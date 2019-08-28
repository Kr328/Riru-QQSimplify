package com.github.kr328.qq.blocker.proxy;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.FileDescriptor;

public class IBinderProxy implements IBinder {
    public interface Callback {
        boolean transact(IBinder original ,int code, Parcel data, Parcel reply, int flags) throws RemoteException;
    }

    private IBinder original;
    private Callback callback;

    public IBinderProxy(IBinder original, Callback callback) {
        this.original = original;
        this.callback = callback;
    }

    @Override
    public String getInterfaceDescriptor() throws RemoteException {
        return original.getInterfaceDescriptor();
    }

    @Override
    public boolean pingBinder() {
        return original.pingBinder();
    }

    @Override
    public boolean isBinderAlive() {
        return original.isBinderAlive();
    }

    @Override
    public IInterface queryLocalInterface(String s) {
        return original.queryLocalInterface(s);
    }

    @Override
    public void dump(FileDescriptor fileDescriptor, String[] strings) throws RemoteException {
        original.dump(fileDescriptor, strings);
    }

    @Override
    public void dumpAsync(FileDescriptor fileDescriptor, String[] strings) throws RemoteException {
        original.dumpAsync(fileDescriptor, strings);
    }

    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return callback.transact(original, code, data, reply, flags);
    }

    @Override
    public void linkToDeath(DeathRecipient deathRecipient, int i) throws RemoteException {
        original.linkToDeath(deathRecipient, i);
    }

    @Override
    public boolean unlinkToDeath(DeathRecipient deathRecipient, int i) {
        return original.unlinkToDeath(deathRecipient, i);
    }
}
