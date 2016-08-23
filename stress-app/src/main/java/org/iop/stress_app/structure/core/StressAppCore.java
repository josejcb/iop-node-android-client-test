package org.iop.stress_app.structure.core;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_osa_addon.layer.linux.database_system.developer.bitdubai.version_1.PluginDatabaseSystemLinuxAddonRoot;
import com.bitdubai.fermat_osa_addon.layer.linux.file_system.developer.bitdubai.version_1.PluginFileSystemLinuxAddonRoot;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantUpdateRegisteredProfileException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.UpdateTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.CantUpdateRegisteredActorException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_pip_addon.layer.platform_service.error_manager.developer.bitdubai.version_1.ErrorManagerPlatformServiceAddonRoot;
import com.bitdubai.fermat_pip_addon.layer.platform_service.event_manager.developer.bitdubai.version_1.EventManagerPlatformServiceAddonRoot;
import com.fermat_p2p_layer.version_1.P2PLayerPluginRoot;
import org.iop.client.version_1.IoPClientPluginRoot;
import org.iop.ns.chat.ChatNetworkServicePluginRoot;
import org.iop.ns.chat.structure.test.MessageReceiver;

import java.util.UUID;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 22/08/16.
 */
public class StressAppCore {

    ChatNetworkServicePluginRoot chatNetworkServicePluginRoot;
    IoPClientPluginRoot ioPClientPluginRoot;
    private ActorProfile profile;
    private ActorProfile lastRemoteProfile;

    //private ActorMessengerManager actorMessengerManager;

    public StressAppCore() {

    }

    public void start(){

        System.out.println("- Starting core ...");

        try {
            //file system
            PluginFileSystemLinuxAddonRoot pluginFileSystemLinuxAddonRoot = new PluginFileSystemLinuxAddonRoot();
            pluginFileSystemLinuxAddonRoot.start();

            //error manager
            ErrorManagerPlatformServiceAddonRoot errorManagerPlatformServiceAddonRoot = new ErrorManagerPlatformServiceAddonRoot();
            errorManagerPlatformServiceAddonRoot.start();

            //event manager
            EventManagerPlatformServiceAddonRoot eventManagerPlatformServiceAddonRoot = new EventManagerPlatformServiceAddonRoot();
            eventManagerPlatformServiceAddonRoot.setErrorManager((ErrorManager) errorManagerPlatformServiceAddonRoot.getManager());
            eventManagerPlatformServiceAddonRoot.start();

            //Database addon
            PluginDatabaseSystemLinuxAddonRoot pluginDatabaseSystemAndroidAddonRoot = new PluginDatabaseSystemLinuxAddonRoot();
            pluginDatabaseSystemAndroidAddonRoot.start();

            //layer
            P2PLayerPluginRoot p2PLayerPluginRoot = new P2PLayerPluginRoot();
            p2PLayerPluginRoot.setEventManager((EventManager) eventManagerPlatformServiceAddonRoot.getManager());
            p2PLayerPluginRoot.start();

            //node
            ioPClientPluginRoot = new IoPClientPluginRoot();
            ioPClientPluginRoot.setPluginFileSystem((PluginFileSystem) pluginFileSystemLinuxAddonRoot.getManager());
            ioPClientPluginRoot.setEventManager((EventManager) eventManagerPlatformServiceAddonRoot.getManager());
            ioPClientPluginRoot.setP2PLayerManager(p2PLayerPluginRoot);
            ioPClientPluginRoot.start();

            //console ns
            /*chatNetworkServicePluginRoot = new ChatNetworkServicePluginRoot();
            chatNetworkServicePluginRoot.setP2PLayerManager(p2PLayerPluginRoot);
            chatNetworkServicePluginRoot.setNetworkClientManager(ioPClientPluginRoot);
            chatNetworkServicePluginRoot.setEventManager((EventManager) eventManagerPlatformServiceAddonRoot.getManager());
            chatNetworkServicePluginRoot.setPluginFileSystem((PluginFileSystem) pluginFileSystemLinuxAddonRoot.getManager());
            chatNetworkServicePluginRoot.setErrorManager((ErrorManager) errorManagerPlatformServiceAddonRoot.getManager());
            chatNetworkServicePluginRoot.setPluginDatabaseSystem((PluginDatabaseSystem) pluginDatabaseSystemAndroidAddonRoot.getManager());
            chatNetworkServicePluginRoot.start();*/

            System.out.println("FERMAT - Network StressAppCore - started satisfactory...");

            while (!ioPClientPluginRoot.isConnected()) {
                System.out.println("Not connected yet");
                Thread.sleep(5000);
            }

            //actorMessengerManager = new ActorMessengerManager(this);

            //Register an actor for testing
            /*profile = new ActorProfile();
            profile.setIdentityPublicKey(UUID.randomUUID().toString());
            System.out.println("I will try to register an actor with pk " + profile.getIdentityPublicKey());
            profile.setActorType(Actors.CHAT.getCode());
            profile.setName("Juan_" + Thread.currentThread().getId());
            profile.setAlias("Alias chat");
            //This represents a valid image
            profile.setPhoto(new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82,
                    0, 0, 0, 15, 0, 0, 0, 15, 8, 6, 0, 0, 0, 59, -42, -107,
                    74, 0, 0, 0, 64, 73, 68, 65, 84, 120, -38, 99, 96, -62, 14, -2,
                    99, -63, 68, 1, 100, -59, -1, -79, -120, 17, -44, -8, 31, -121, 28, 81,
                    26, -1, -29, 113, 13, 78, -51, 100, -125, -1, -108, 24, 64, 86, -24, -30,
                    11, 101, -6, -37, 76, -106, -97, 25, 104, 17, 96, -76, 77, 97, 20, -89,
                    109, -110, 114, 21, 0, -82, -127, 56, -56, 56, 76, -17, -42, 0, 0, 0,
                    0, 73, 69, 78, 68, -82, 66, 96, -126});
            profile.setNsIdentityPublicKey(chatNetworkServicePluginRoot.getNetWorkServicePublicKey());
            profile.setExtraData("Test extra data");
            chatNetworkServicePluginRoot.registerActor(profile, 0, 0);*/

        }catch (Exception e){
            System.out.println("StressAppCore Exception "+e);
            e.printStackTrace();
        }
    }

    public void setProfile(ActorProfile profile) {
        if (profile!=null) {
            this.profile = profile;
            chatNetworkServicePluginRoot.registerProfile(profile);
        }else{
            try {
                chatNetworkServicePluginRoot.updateRegisteredActor(profile, UpdateTypes.FULL);
            } catch (Exception e){
                System.out.println("StressAppCore Exception "+e);
                e.printStackTrace();
            }
        }
    }

    public ChatNetworkServicePluginRoot getChatNetworkServicePluginRoot() {
        return chatNetworkServicePluginRoot;
    }

    public void setReceiver(MessageReceiver eventReceiver){
        chatNetworkServicePluginRoot.setMessageReceiver(eventReceiver);
    }

    public ActorProfile getProfile() {
        return profile;
    }

    public void setLastRemoteProfile(ActorProfile lastRemoteProfile) {
        this.lastRemoteProfile = lastRemoteProfile;
    }

    public ActorProfile getLastRemoteProfile() {
        return this.lastRemoteProfile;
    }

    public boolean isConnected(){
        return ioPClientPluginRoot.isConnected();
    }

    public void shutdown() {
        ioPClientPluginRoot.disconnect();
    }
}
