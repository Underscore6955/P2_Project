package com.example.p2project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.p2project.IdleGame.DualSoundManager; // Make sure this import matches your DualSoundManager's package

import java.util.ArrayList;
import java.util.List;

public class AudioPlayer extends AppCompatActivity {

    private DualSoundManager soundManager;
    private ImageView sound1Image, sound2Image, sound1Heart, sound2Heart, btnPlayPause;
    private TextView sound1Text, sound2Text;
    private SeekBar seekbarVolume, seekbarPitch;

    private Track currentTrack1 = null;
    private Track currentTrack2 = null;
    private List<Track> availableTracks;

    // Helper class to hold track data
    private static class Track {
        int rawId;
        String name;
        int imageId;

        Track(int rawId, String name, int imageId) {
            this.rawId = rawId;
            this.name = name;
            this.imageId = imageId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Boilerplate calibration
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_player);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.music_player), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Audio logic
        soundManager = new DualSoundManager(this);
        initViews();
        setupTracks();
        setupListeners();
        updateUI();
    }

    private void initViews() {
        sound1Image = findViewById(R.id.sound1_image);
        sound2Image = findViewById(R.id.sound2_image);
        sound1Text = findViewById(R.id.sound1_text);
        sound2Text = findViewById(R.id.sound2_text);
        sound1Heart = findViewById(R.id.sound1_heart);
        sound2Heart = findViewById(R.id.sound2_heart);

        btnPlayPause = findViewById(R.id.btn_play_pause);

        seekbarVolume = findViewById(R.id.seekbar_volume);
        seekbarPitch = findViewById(R.id.seekbar_pitch);
    }

    private void setupTracks() {
        availableTracks = new ArrayList<>();
        // rawId -1 = "Remove sound"
        availableTracks.add(new Track(-1, "Remove sound", R.drawable.remove_24px));

        // All the audio tracks, with names and icons to label them
        availableTracks.add(new Track(R.raw.autumn_forest_floor, "Autumn Forest", R.drawable.forest_24px));
        availableTracks.add(new Track(R.raw.deep_forest_rain, "Deep Forest Rain", R.drawable.heavy_rain24px));
        availableTracks.add(new Track(R.raw.fireplace, "Fireplace", R.drawable.fireplace_24px));
        availableTracks.add(new Track(R.raw.heavy_rain, "Heavy Rain", R.drawable.heavy_rain24px));
        availableTracks.add(new Track(R.raw.jungle_floor, "Jungle Floor", R.drawable.nature_24px));
        availableTracks.add(new Track(R.raw.light_rain, "Light Rain", R.drawable.heavy_rain24px));
        availableTracks.add(new Track(R.raw.spring_forest_floor, "Spring Forest", R.drawable.forest_24px));
        availableTracks.add(new Track(R.raw.village_night, "Village Night", R.drawable.village_24px));
        availableTracks.add(new Track(R.raw.wind_turbine, "Wind Turbine", R.drawable.turbine_24px));
        availableTracks.add(new Track(R.raw.windy_cornfield, "Windy Cornfield", R.drawable.cornfield_24px));
        availableTracks.add(new Track(R.raw.windy_grassy_field, "Windy Grass", R.drawable.grass_24px));
    }

    private void setupListeners() {
        // Sound Selection clicks
        sound1Image.setOnClickListener(v -> showSoundSelectionDialog(1));
        sound2Image.setOnClickListener(v -> showSoundSelectionDialog(2));

        // Play/pause Click
        if (btnPlayPause != null) {
            btnPlayPause.setOnClickListener(v -> {
                if (soundManager.isAnyPlaying()) {
                    soundManager.pauseAll();
                } else {
                    soundManager.resumeAll();
                }
                updatePlayPauseIcon();
            });
        }

        // Favorite clicks
        sound1Heart.setOnClickListener(v -> toggleFavoriteForSlot(1));
        sound2Heart.setOnClickListener(v -> toggleFavoriteForSlot(2));

        // Sliders
        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundManager.setMasterVolume(progress / 100f); // 0.0 to 1.0
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekbarPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Sets the boundaries for volume, ensuring no program crashes
                float pitch;
                if (progress < 50) {
                    pitch = 0.5f + (progress / 100f);
                } else {
                    pitch = 1.0f + ((progress - 50) / 50f);
                }
                soundManager.setMasterPitch(pitch);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    private void showSoundSelectionDialog(int slotNumber) {
        // Shows the pop-up with the audio files
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Sound");

        // Pop-up grid construction
        GridView gridView = new GridView(this);
        gridView.setNumColumns(3);
        gridView.setPadding(16, 16, 16, 16);

        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() { return availableTracks.size(); }
            @Override
            public Object getItem(int position) { return availableTracks.get(position); }
            @Override
            public long getItemId(int position) { return position; }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_sound_grid, parent, false);
                }
                ImageView img = convertView.findViewById(R.id.grid_item_image);
                TextView txt = convertView.findViewById(R.id.grid_item_text);

                Track track = availableTracks.get(position);
                img.setImageResource(track.imageId);
                txt.setText(track.name);
                return convertView;
            }
        });

        AlertDialog dialog = builder.setView(gridView).create();

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Track selectedTrack = availableTracks.get(position);
            setTrackToSlot(slotNumber, selectedTrack);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setTrackToSlot(int slotNumber, Track track) {
        if (slotNumber == 1) {
            if (track.rawId == -1) {
                soundManager.removeSound1();
                currentTrack1 = null;
            } else {
                currentTrack1 = track;
                soundManager.PlaySound1(track.rawId);
            }
        } else {
            if (track.rawId == -1) {
                soundManager.removeSound2();
                currentTrack2 = null;
            } else {
                currentTrack2 = track;
                soundManager.playSound2(track.rawId);
            }
        }
        updateUI();
    }

    private void toggleFavoriteForSlot(int slotNumber) {
        Track track = (slotNumber == 1) ? currentTrack1 : currentTrack2;
        if (track != null && track.rawId != -1) {
            soundManager.toggleFavorite(track.rawId);
            updateUI();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() { // Adding icons to UI
        // Slot 1
        if (currentTrack1 == null) {
            sound1Image.setImageResource(R.drawable.add_box_24px);
            sound1Text.setText("Add a sound");
            sound1Heart.setVisibility(View.INVISIBLE);
        } else {
            sound1Image.setImageResource(currentTrack1.imageId);
            sound1Text.setText(currentTrack1.name);
            sound1Heart.setVisibility(View.VISIBLE);
            sound1Heart.setImageResource(soundManager.isFavorite(currentTrack1.rawId)
                    ? R.drawable.checked_favorite_24px : R.drawable.uchecked_favorite_24px);
        }

        // Slot 2
        if (currentTrack2 == null) {
            sound2Image.setImageResource(R.drawable.add_box_24px);
            sound2Text.setText("Add a sound");
            sound2Heart.setVisibility(View.INVISIBLE);
        } else {
            sound2Image.setImageResource(currentTrack2.imageId);
            sound2Text.setText(currentTrack2.name);
            sound2Heart.setVisibility(View.VISIBLE);
            sound2Heart.setImageResource(soundManager.isFavorite(currentTrack2.rawId)
                    ? R.drawable.checked_favorite_24px : R.drawable.uchecked_favorite_24px);
        }

        updatePlayPauseIcon();
    }

    private void updatePlayPauseIcon() {
        if (btnPlayPause != null) {
            if (soundManager.isAnyPlaying()) {
                btnPlayPause.setImageResource(R.drawable.pause_circle_24px);
            } else {
                btnPlayPause.setImageResource(R.drawable.play_circle_24px);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.releaseAll(); // No memory leaks
        }
    }
}