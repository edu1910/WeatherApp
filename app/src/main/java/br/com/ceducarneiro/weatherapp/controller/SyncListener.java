package br.com.ceducarneiro.weatherapp.controller;

import java.util.List;

public interface SyncListener {
    void onSyncStart(List args);
    void onSyncFinish(List args);

    static final String ON_SYNC_START = "onSyncStart";
    static final String ON_SYNC_FINISH = "onSyncFinish";
}
