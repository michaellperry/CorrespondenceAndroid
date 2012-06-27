package com.updatecontrols.correspondence.binary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.updatecontrols.correspondence.memento.CorrespondenceFactType;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.FactTreeMemento;
import com.updatecontrols.correspondence.memento.IdentifiedFactBase;
import com.updatecontrols.correspondence.memento.IdentifiedFactMemento;
import com.updatecontrols.correspondence.memento.IdentifiedFactRemote;
import com.updatecontrols.correspondence.memento.PredecessorMemento;
import com.updatecontrols.correspondence.memento.RoleMemento;

public class FactTreeSerializer {
	
    private ArrayList<CorrespondenceFactType> factTypes = new ArrayList<CorrespondenceFactType>();
    private ArrayList<RoleMemento> roles = new ArrayList<RoleMemento>();

    public void serlializeFactTree(FactTreeMemento factTreeMemento, DataOutputStream factWriter) throws IOException {
        collectFactTypesAndRoles(factTreeMemento);

        factWriter.writeLong(factTreeMemento.getDatabaseId());

        factWriter.writeShort((short)factTypes.size());
        for (CorrespondenceFactType factType : factTypes) {
        	BinaryHelper.writeString(factWriter, factType.getName());
        	factWriter.writeInt(factType.getVersion());
        }

        factWriter.writeShort((short)roles.size());
        for (RoleMemento role : roles) {
        	factWriter.writeShort(getFactTypeId(role.getDeclaringType()));
        	BinaryHelper.writeString(factWriter, role.getRoleName());
        }

        short factCount = (short)factTreeMemento.getFactCount();
        factWriter.writeShort(factCount);
        for (IdentifiedFactBase fact : factTreeMemento.getFacts()) {
            if (fact instanceof IdentifiedFactMemento)
                serializeIdentifiedFactMemento((IdentifiedFactMemento)fact, factWriter);
            else
                serializeIdentifiedFactRemote((IdentifiedFactRemote)fact, factWriter);
        }
    }

    private void collectFactTypesAndRoles(FactTreeMemento factTreeMemento) {
        for (IdentifiedFactBase fact : factTreeMemento.getFacts()) {
            if (fact instanceof IdentifiedFactMemento) {
                addFactType(((IdentifiedFactMemento)fact).getMemento().getType());
                for (PredecessorMemento predecessor : ((IdentifiedFactMemento)fact).getMemento().getPredecessors()) {
                    addFactType(predecessor.getRole().getDeclaringType());
                    addRole(predecessor.getRole());
                }
            }
        }
    }

    private void addFactType(CorrespondenceFactType factType) {
        if (!factTypes.contains(factType))
            factTypes.add(factType);
    }

    private void addRole(RoleMemento role) {
        if (!roles.contains(role))
            roles.add(role);
    }

    private short getFactTypeId(CorrespondenceFactType factType) {
        return (short)factTypes.indexOf(factType);
    }

    private short getRoleId(RoleMemento role) {
        return (short)roles.indexOf(role);
    }

    private CorrespondenceFactType getFactType(short factTypeId) {
        if (0 > factTypeId || factTypeId >= factTypes.size())
            throw new RuntimeException("Fact type id " + factTypeId + " is out of range.");
        return factTypes.get(factTypeId);
    }

    private RoleMemento getRole(short roleId) {
        if (0 > roleId || roleId >= roles.size())
            throw new RuntimeException("Role id " + roleId + " is out of range.");
        return roles.get(roleId);
    }

    private void serializeIdentifiedFactMemento(IdentifiedFactMemento factMemento, DataOutputStream factWriter) throws IOException {
    	factWriter.writeLong(factMemento.getId().getKey());
    	factWriter.writeBoolean(true);
    	factWriter.writeShort(getFactTypeId(factMemento.getMemento().getType()));
        short dataSize = factMemento.getMemento().getData() == null ? (short)0 : (short)(factMemento.getMemento().getData().length);
        factWriter.writeShort(dataSize);
        if (dataSize != 0)
            factWriter.write(factMemento.getMemento().getData());

        short predecessorsCount = (short)factMemento.getMemento().getPredecessorCount();
        factWriter.writeShort(predecessorsCount);
        for (PredecessorMemento predecessor : factMemento.getMemento().getPredecessors()) {
        	factWriter.writeShort(getRoleId(predecessor.getRole()));
        	factWriter.writeBoolean(predecessor.isPivot());
        	factWriter.writeLong(predecessor.getId().getKey());
        }
    }

    private void serializeIdentifiedFactRemote(IdentifiedFactRemote identifiedFactRemote, DataOutputStream factWriter) throws IOException {
    	factWriter.writeLong(identifiedFactRemote.getId().getKey());
    	factWriter.writeBoolean(false);
    	factWriter.writeLong(identifiedFactRemote.getRemoteId().getKey());
    }

    public FactTreeMemento deserializeFactTree(DataInputStream factReader) throws IOException {
        long databaseId = factReader.readLong();
        FactTreeMemento factTreeMemento = new FactTreeMemento(databaseId);

        short factTypeCount = factReader.readShort();
        for (short i = 0; i < factTypeCount; i++) {
            String typeName = BinaryHelper.readString(factReader);
            int version = factReader.readInt();
            factTypes.add(new CorrespondenceFactType(typeName, version));
        }

        short roleCount = factReader.readShort();
        for (short i = 0; i < roleCount; i++) {
            short factTypeId = factReader.readShort();
            String roleName = BinaryHelper.readString(factReader);
            roles.add(new RoleMemento(getFactType(factTypeId), roleName, null, false));
        }

        short factCount = factReader.readShort();
        for (short i = 0; i < factCount; i++) {
            factTreeMemento.add(deserlializeFact(factReader));
        }
        return factTreeMemento;
    }

    private IdentifiedFactBase deserlializeFact(DataInputStream factReader) throws IOException {
        long factId;
        boolean isMemento;

        factId = factReader.readLong();
        isMemento = factReader.readBoolean();
        if (isMemento)
            return deserlializeIdentifiedFactMemento(factReader, factId);
        else
            return deserializeIdentifiedFactRemote(factReader, factId);
    }

    private IdentifiedFactMemento deserlializeIdentifiedFactMemento(DataInputStream factReader, long factId) throws IOException {
        short dataSize;
        byte[] data;
        short predecessorCount;

        CorrespondenceFactType factType = getFactType(factReader.readShort());
        dataSize = factReader.readShort();
        data = new byte[dataSize];
        if (dataSize > 0)
        	factReader.readFully(data);
        predecessorCount = factReader.readShort();

        FactMemento factMemento = new FactMemento(factType);
        factMemento.setData(data);
        for (short i = 0; i < predecessorCount; i++) {
            boolean isPivot;
            long predecessorFactId;

            RoleMemento role = getRole(factReader.readShort());
            isPivot = factReader.readBoolean();
            predecessorFactId = factReader.readLong();

            factMemento.addPredecessor(
                role,
                new FactID(predecessorFactId),
                isPivot
            );
        }

        return new IdentifiedFactMemento(new FactID(factId), factMemento);
    }

    private IdentifiedFactRemote deserializeIdentifiedFactRemote(DataInputStream factReader, long factId) throws IOException {
        long remoteFactId;

        remoteFactId = factReader.readLong();
        return new IdentifiedFactRemote(new FactID(factId), new FactID(remoteFactId));
    }
}
