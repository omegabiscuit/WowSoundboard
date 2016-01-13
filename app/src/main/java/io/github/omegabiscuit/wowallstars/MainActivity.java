package io.github.omegabiscuit.wowallstars;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private View mRoot;
    private PopupMenu mLeeroyPopupMenu;
    private PopupMenu mDotsPopupMenu;
    private PopupMenu mLeatherPopupMenu;
    private PopupMenu mRedShirtPopupMenu;

    private MediaPlayer mp;
    private int mSoundResId;

    private final View.OnClickListener mOnOverflowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.leeroy_overflow:
                    mSoundResId = R.raw.leeroy;
                    mLeeroyPopupMenu.show();
                    break;
                case R.id.dots_overflow:
                    mSoundResId = R.raw.moredots;
                    mDotsPopupMenu.show();
                    break;
                case R.id.leather_overflow:
                    mSoundResId = R.raw.leatherbelt;
                    mLeatherPopupMenu.show();
                    break;
                case R.id.redShirt_overflow:
                    mSoundResId = R.raw.redshirtkid;
                    mRedShirtPopupMenu.show();
                    break;
            }
        }
    };

    private final PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_save:
                    String path = SoundUtil.saveNotificationTone(MainActivity.this, mSoundResId);
                    if (!TextUtils.isEmpty(path)) {
                        Snackbar.make(mRoot, R.string.sound_saved_successfully, Snackbar.LENGTH_SHORT)
                                .show();
                    } else {
                        Snackbar.make(mRoot, R.string.failed_to_save_sound, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    return true;
                case R.id.action_set_as_notification_tone:
                    boolean success = SoundUtil.setTone(MainActivity.this, mSoundResId);
                    if (success) {
                        Snackbar.make(mRoot, R.string.notification_tone_set, Snackbar.LENGTH_SHORT)
                                .show();
                    } else {
                        Snackbar.make(mRoot, R.string.failed_to_set_notification_tone, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = findViewById(R.id.root);
        Button Leeroy = (Button) findViewById(R.id.Leeroy);
        Button Dots = (Button) findViewById(R.id.Dots);
        Button Leather = (Button) findViewById(R.id.Leather);
        Button redShirt = (Button) findViewById(R.id.redShirt);
        View leeroyOverflow = findViewById(R.id.leeroy_overflow);
        View dotsOverflow = findViewById(R.id.dots_overflow);
        View leatherOverflow = findViewById(R.id.leather_overflow);
        View redShirtOverflow = findViewById(R.id.redShirt_overflow);
        Leeroy.setOnClickListener(this);
        Dots.setOnClickListener(this);
        Leather.setOnClickListener(this);
        redShirt.setOnClickListener(this);

        leeroyOverflow.setOnClickListener(mOnOverflowClickListener);
        dotsOverflow.setOnClickListener(mOnOverflowClickListener);
        leatherOverflow.setOnClickListener(mOnOverflowClickListener);
        redShirtOverflow.setOnClickListener(mOnOverflowClickListener);

        mLeeroyPopupMenu = new PopupMenu(this, leeroyOverflow);
        mLeeroyPopupMenu.getMenuInflater().inflate(R.menu.menu_sound, mLeeroyPopupMenu.getMenu());
        mLeeroyPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);

        mDotsPopupMenu = new PopupMenu(this, dotsOverflow);
        mDotsPopupMenu.getMenuInflater().inflate(R.menu.menu_sound, mDotsPopupMenu.getMenu());
        mDotsPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);

        mLeatherPopupMenu = new PopupMenu(this, leatherOverflow);
        mLeatherPopupMenu.getMenuInflater().inflate(R.menu.menu_sound, mLeatherPopupMenu.getMenu());
        mLeatherPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);

        mRedShirtPopupMenu = new PopupMenu(this, redShirtOverflow);
        mRedShirtPopupMenu.getMenuInflater().inflate(R.menu.menu_sound, mRedShirtPopupMenu.getMenu());
        mRedShirtPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
    }
    public void onClick(View v) {
        int name=0;
        switch (v.getId()){
            case R.id.Leeroy:
                name = R.raw.leeroy;
                break;
            case R.id.Dots:
                name = R.raw.moredots;
                break;
            case R.id.redShirt:
                name = R.raw.redshirtkid;
                break;
            case R.id.Leather:
                name = R.raw.leatherbelt;
        }

        if (mp != null){
            mp.release();
        }
        mp = MediaPlayer.create(this, name);
        mp.start();
    }

}
