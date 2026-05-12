package net.kdt.pojavlaunch.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ExpandableListAdapter;

import net.kdt.pojavlaunch.JavaGUILauncherActivity;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.modloaders.ForgeUtils;
import net.kdt.pojavlaunch.modloaders.ModloaderListenerProxy;
import net.kdt.pojavlaunch.modloaders.NeoForgeDownloadTask;
import net.kdt.pojavlaunch.modloaders.NeoForgeUtils;
import net.kdt.pojavlaunch.modloaders.NeoForgeVersionListAdapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NeoForgeInstallFragment extends ModVersionListFragment<List<String>> {
    public static final String TAG = "NeoForgeInstallFragment";

    public NeoForgeInstallFragment() {
        super(TAG);
    }

    @Override
    public int getTitleText() {
        return R.string.neoforge_dl_select_version;
    }

    @Override
    public int getNoDataMsg() {
        return R.string.neoforge_dl_no_installer;
    }

    @Override
    public List<String> loadVersionList() throws IOException {
        return NeoForgeUtils.downloadNeoForgeVersions();
    }

    @Override
    public ExpandableListAdapter createAdapter(List<String> versionList, LayoutInflater layoutInflater) {
        return new NeoForgeVersionListAdapter(versionList, layoutInflater);
    }

    @Override
    public Runnable createDownloadTask(Object selectedVersion, ModloaderListenerProxy listenerProxy) {
        return new NeoForgeDownloadTask(listenerProxy, (String) selectedVersion);
    }

    @Override
    public void onDownloadFinished(Context context, File downloadedFile) {
        Intent modInstallerStartIntent = new Intent(context, JavaGUILauncherActivity.class);
        ForgeUtils.addAutoInstallArgs(modInstallerStartIntent, downloadedFile, true);
        context.startActivity(modInstallerStartIntent);
    }
}
