package com.example.sherlock.drumsticks;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by sherlock on 25/4/17.
 */

public class soundPool {

    private SoundPool mShortPlayer= null;
    private HashMap mSounds = new HashMap();
    public soundPool(Context pContext)
    {
        // setup Soundpool

        this.mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSounds.put(R.raw.crash, this.mShortPlayer.load(pContext, R.raw.crash, 1));
        mSounds.put(R.raw.hithat, this.mShortPlayer.load(pContext, R.raw.hithat, 1));
        mSounds.put(R.raw.snare, this.mShortPlayer.load(pContext, R.raw.snare, 1));
    }

    public void playShortResource(int piResource) {
        int iSoundId = (Integer) mSounds.get(piResource);
        this.mShortPlayer.play(iSoundId, 0.99f, 0.99f, 0, 0, 1);
    }

    // Cleanup
    public void release() {
        // Cleanup
        this.mShortPlayer.release();
        this.mShortPlayer = null;
    }

}
