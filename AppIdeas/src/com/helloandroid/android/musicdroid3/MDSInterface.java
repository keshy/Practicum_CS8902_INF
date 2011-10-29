/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\Documents and Settings\zhobbs\workspace\MusicDroid3\src\com\helloandroid\android\musicdroid3\MDSInterface.aidl
 */
package com.helloandroid.android.musicdroid3;
import java.lang.String;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.BinderNative;
import android.os.Parcel;
public interface MDSInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.BinderNative implements com.helloandroid.android.musicdroid3.MDSInterface
{
private static final java.lang.String DESCRIPTOR = "com.helloandroid.android.musicdroid3.MDSInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an MDSInterface interface,
 * generating a proxy if needed.
 */
public static com.helloandroid.android.musicdroid3.MDSInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
com.helloandroid.android.musicdroid3.MDSInterface in = (com.helloandroid.android.musicdroid3.MDSInterface)obj.queryLocalInterface(DESCRIPTOR);
if ((in!=null)) {
return in;
}
return new com.helloandroid.android.musicdroid3.MDSInterface.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
{
try {
switch (code)
{
case TRANSACTION_clearPlaylist:
{
this.clearPlaylist();
return true;
}
case TRANSACTION_addSongPlaylist:
{
java.lang.String _arg0;
_arg0 = data.readString();
this.addSongPlaylist(_arg0);
return true;
}
case TRANSACTION_playFile:
{
int _arg0;
_arg0 = data.readInt();
this.playFile(_arg0);
return true;
}
case TRANSACTION_pause:
{
this.pause();
return true;
}
case TRANSACTION_stop:
{
this.stop();
return true;
}
case TRANSACTION_skipForward:
{
this.skipForward();
return true;
}
case TRANSACTION_skipBack:
{
this.skipBack();
return true;
}
}
}
catch (android.os.DeadObjectException e) {
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.helloandroid.android.musicdroid3.MDSInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public void clearPlaylist() throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
mRemote.transact(Stub.TRANSACTION_clearPlaylist, _data, null, 0);
}
finally {
_data.recycle();
}
}
public void addSongPlaylist(java.lang.String song) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeString(song);
mRemote.transact(Stub.TRANSACTION_addSongPlaylist, _data, null, 0);
}
finally {
_data.recycle();
}
}
public void playFile(int position) throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_playFile, _data, null, 0);
}
finally {
_data.recycle();
}
}
public void pause() throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
mRemote.transact(Stub.TRANSACTION_pause, _data, null, 0);
}
finally {
_data.recycle();
}
}
public void stop() throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
mRemote.transact(Stub.TRANSACTION_stop, _data, null, 0);
}
finally {
_data.recycle();
}
}
public void skipForward() throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
mRemote.transact(Stub.TRANSACTION_skipForward, _data, null, 0);
}
finally {
_data.recycle();
}
}
public void skipBack() throws android.os.DeadObjectException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
mRemote.transact(Stub.TRANSACTION_skipBack, _data, null, 0);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_clearPlaylist = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_addSongPlaylist = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_playFile = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_pause = (IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_stop = (IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_skipForward = (IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_skipBack = (IBinder.FIRST_CALL_TRANSACTION + 6);
}
public void clearPlaylist() throws android.os.DeadObjectException;
public void addSongPlaylist(java.lang.String song) throws android.os.DeadObjectException;
public void playFile(int position) throws android.os.DeadObjectException;
public void pause() throws android.os.DeadObjectException;
public void stop() throws android.os.DeadObjectException;
public void skipForward() throws android.os.DeadObjectException;
public void skipBack() throws android.os.DeadObjectException;
}
