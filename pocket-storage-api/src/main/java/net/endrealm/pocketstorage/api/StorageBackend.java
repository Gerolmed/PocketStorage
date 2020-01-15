package net.endrealm.pocketstorage.api;

public interface StorageBackend {
    String getBackendId();
    PocketWorld getPocketWorld(String identifier);
    boolean saveWorld(String identifier, PocketWorld world);
    boolean containsWorld(String identifier);
}
