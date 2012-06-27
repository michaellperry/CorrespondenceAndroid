package com.facetedworlds.honeydolist.model;

public class CorrespondenceModel implements com.updatecontrols.correspondence.Module {
	public void registerTypes(com.updatecontrols.correspondence.Community community) throws com.updatecontrols.correspondence.CorrespondenceException {
		community
			.addType( Domain.TYPE , new Domain.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Identity.TYPE , new Identity.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Identity__name.TYPE , new Identity__name.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( PowerUser.TYPE , new PowerUser.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Email.TYPE , new Email.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( EmailIdentityGrant.TYPE , new EmailIdentityGrant.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( EmailIdentityRevoke.TYPE , new EmailIdentityRevoke.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( IdentityListShare.TYPE , new IdentityListShare.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( IdentityListSharePatch.TYPE , new IdentityListSharePatch.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( IdentityListRevoke.TYPE , new IdentityListRevoke.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Avatar.TYPE , new Avatar.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( List.TYPE , new List.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( List__name.TYPE , new List__name.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( List__avatar.TYPE , new List__avatar.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( ListContents.TYPE , new ListContents.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Task.TYPE , new Task.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Task__text.TYPE , new Task__text.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Task__context.TYPE , new Task__context.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( Context.TYPE , new Context.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( TaskComplete.TYPE , new TaskComplete.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( TaskCompleteUndo.TYPE , new TaskCompleteUndo.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addType( IdentityListShare__ordinal.TYPE , new IdentityListShare__ordinal.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )
			.addQuery( Domain.TYPE , Domain.QUERY_unpatchedShares() )
			.addQuery( Identity.TYPE , Identity.QUERY_nameCandidates() )
			.addQuery( Identity.TYPE , Identity.QUERY_activeShares() )
			.addQuery( Identity.TYPE , Identity.QUERY_sharedLists() )
			.addQuery( Identity.TYPE , Identity.QUERY_sharedListContents() )
			.addQuery( Identity.TYPE , Identity.QUERY_openTasks() )
			.addQuery( Identity.TYPE , Identity.QUERY_powerUserCandidates() )
			.addQuery( Identity.TYPE , Identity.QUERY_listsNeedingContents() )
			.addQuery( Identity__name.TYPE , Identity__name.QUERY_isCurrent() )
			.addQuery( IdentityListShare.TYPE , IdentityListShare.QUERY_ordinal() )
			.addQuery( IdentityListShare.TYPE , IdentityListShare.QUERY_isActive() )
			.addQuery( IdentityListShare.TYPE , IdentityListShare.QUERY_isPatched() )
			.addQuery( List.TYPE , List.QUERY_nameCandidates() )
			.addQuery( List.TYPE , List.QUERY_avatarCandidates() )
			.addQuery( List.TYPE , List.QUERY_tasks() )
			.addQuery( List.TYPE , List.QUERY_hasListContents() )
			.addQuery( List__name.TYPE , List__name.QUERY_isCurrent() )
			.addQuery( List__avatar.TYPE , List__avatar.QUERY_isCurrent() )
			.addQuery( Task.TYPE , Task.QUERY_complete() )
			.addQuery( Task.TYPE , Task.QUERY_isComplete() )
			.addQuery( Task.TYPE , Task.QUERY_textCandidates() )
			.addQuery( Task.TYPE , Task.QUERY_contextCandidates() )
			.addQuery( Task__text.TYPE , Task__text.QUERY_isCurrent() )
			.addQuery( Task__context.TYPE , Task__context.QUERY_isCurrent() )
			.addQuery( TaskComplete.TYPE , TaskComplete.QUERY_isUndone() )
			;
	}
}