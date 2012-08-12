package com.updatecontrols.correspondence.binary;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactTreeMemento;
import com.updatecontrols.correspondence.memento.PivotMemento;
import com.updatecontrols.correspondence.memento.TimestampID;
import com.updatecontrols.correspondence.memento.UnpublishMemento;
import com.updatecontrols.correspondence.strategy.AsynchronousCommunicationStrategy;
import com.updatecontrols.correspondence.strategy.ErrorCallback;
import com.updatecontrols.correspondence.strategy.GetManyCallback;
import com.updatecontrols.correspondence.strategy.MessageReceivedListener;
import com.updatecontrols.correspondence.strategy.PostCallback;
import com.updatecontrols.correspondence.strategy.PushSubscription;

public class BinaryHTTPAsynchronousCommunicationStrategy implements
		AsynchronousCommunicationStrategy {
	
	private static final byte ProtocolVersion = 2;
	private static final byte GetManyRequestToken = 1;
	protected static final int PostRequestToken = 2;
	
	private HTTPConfigurationProvider configurationProvider;

	private Thread thread;
	private BlockingQueue<Runnable> actionQueue = new LinkedBlockingQueue<Runnable>();
	
	public BinaryHTTPAsynchronousCommunicationStrategy(HTTPConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				threadProc();
			}
		},
		"BinaryHTTPAsynchronousCommunicationStrategy");
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void addMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beginGetMany(final FactTreeMemento pivotTree,
			final List<PivotMemento> pivots, final UUID clientGuid,
			final GetManyCallback success, final ErrorCallback error) {
		queueNextAction(new Runnable() {
			
			@Override
			public void run() {
				HTTPConfiguration configuration = configurationProvider.getConfiguration();
				
				BufferedReader in = null;
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					DataOutputStream writer = new DataOutputStream(out);
					
					writer.writeByte(ProtocolVersion);
					String domain = configuration.getApiKey();
					BinaryHelper.writeString(writer, domain);
					writer.writeByte(GetManyRequestToken);
					FactTreeSerializer serializer = new FactTreeSerializer();
					serializer.serlializeFactTree(pivotTree, writer);
					short pivotCount = (short)pivots.size();
					writer.writeShort(pivotCount);
					for (PivotMemento pivot : pivots) {
						writer.writeLong(pivot.getPivotId().getKey());
						writer.writeLong(pivot.getTimestamp().getKey());
					}
					BinaryHelper.writeString(writer, clientGuid.toString());
					int timeoutSeconds = 0;
					writer.writeInt(timeoutSeconds);
					
					byte[] buffer = out.toByteArray();
					
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(configuration.getEndpoint());
					request.setEntity(new ByteArrayEntity(buffer));
					HttpResponse response = client.execute(request);
					
					DataInputStream reader = new DataInputStream(response.getEntity().getContent());
					byte version = reader.readByte();
					if (version != ProtocolVersion)
						throw new IllegalStateException("Unknown response version from GetMany request.");
					byte responseToken = reader.readByte();
					if (responseToken != 1)
						throw new IllegalStateException("Unknown response token from GetMany request.");
					FactTreeSerializer deserializer = new FactTreeSerializer();
					FactTreeMemento responseTree = deserializer.deserializeFactTree(reader);
					short responsePivotCount = reader.readShort();
					ArrayList<PivotMemento> responsePivots = new ArrayList<PivotMemento>(responsePivotCount);
					for (short pivotIndex = 0; pivotIndex < responsePivotCount; ++pivotIndex) {
						long pivotId = reader.readLong();
						long timestamp = reader.readLong();
						responsePivots.add(new PivotMemento(new FactID(pivotId), new TimestampID(0, timestamp)));
					}
					success.onSuccess(responseTree, responsePivots);
				} catch (Exception e) {
					error.onError(e);
				}
				finally {
			        if (in != null) {
			            try {
			                in.close();
			            }
			            catch (IOException e) {
			            }
			        }
				}
			}
		});
	}

	@Override
	public void beginPost(final FactTreeMemento messageBody, final UUID clientGuid,
			final List<UnpublishMemento> unpublishedMessages, final PostCallback success,
			final ErrorCallback error) {
		queueNextAction(new Runnable() {
			
			@Override
			public void run() {
				HTTPConfiguration configuration = configurationProvider.getConfiguration();
				
				BufferedReader in = null;
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					DataOutputStream writer = new DataOutputStream(out);
					
					writer.writeByte(ProtocolVersion);
					String domain = configuration.getApiKey();
					BinaryHelper.writeString(writer, domain);
					writer.writeByte(PostRequestToken);
					FactTreeSerializer serializer = new FactTreeSerializer();
					for (UnpublishMemento unpublishedMessage : unpublishedMessages) {
						serializer.addFactType(unpublishedMessage.getRole().getDeclaringType());
						serializer.addRole(unpublishedMessage.getRole());
					}
					serializer.serlializeFactTree(messageBody, writer);
					BinaryHelper.writeString(writer, clientGuid.toString());
					short unpublishedMessageCount = (short)unpublishedMessages.size();
					writer.writeShort(unpublishedMessageCount);
					for (UnpublishMemento unpublishedMessage : unpublishedMessages) {
						writer.writeLong(unpublishedMessage.getMessageId().getKey());
						writer.writeLong(serializer.getRoleId(unpublishedMessage.getRole()));
					}
					
					byte[] buffer = out.toByteArray();
					
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(configuration.getEndpoint());
					request.setEntity(new ByteArrayEntity(buffer));
					HttpResponse response = client.execute(request);
					
					DataInputStream reader = new DataInputStream(response.getEntity().getContent());
					byte version = reader.readByte();
					if (version != ProtocolVersion)
						throw new IllegalStateException("Unknown response version from Post request.");
					byte responseToken = reader.readByte();
					if (responseToken != 2)
						throw new IllegalStateException("Unknown response token from Post request.");
					success.onSuccess();
				} catch (Exception e) {
					error.onError(e);
				}
				finally {
			        if (in != null) {
			            try {
			                in.close();
			            }
			            catch (IOException e) {
			            }
			        }
				}
			}
		});
	}

	@Override
	public String getPeerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProtocolName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLongPolling() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PushSubscription subscribeForPush(FactTreeMemento pivotTree,
			FactID pivotID, UUID clientGuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void interrupt(UUID clientDatabaseGuid) {
		// TODO Auto-generated method stub
		
	}

	private void threadProc() {
		try {
			while (true) {
				Runnable nextAction = actionQueue.take();
				nextAction.run();
			}
		}
		catch (InterruptedException e) {
			// Exit the thread.
		}
	}

	private void queueNextAction(Runnable action) {
		try {
			actionQueue.put(action);
		}
		catch (InterruptedException e) {
			// Don't take the action. We're shutting down.
		}
	}
}
