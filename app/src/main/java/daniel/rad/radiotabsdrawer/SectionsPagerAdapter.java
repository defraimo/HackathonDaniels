package daniel.rad.radiotabsdrawer;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import daniel.rad.radiotabsdrawer.playlist.PlaylistFragment;
import daniel.rad.radiotabsdrawer.programs.ProgramsFragment;
import daniel.rad.radiotabsdrawer.radioFragments.RadioFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        String[] tabNames = new String[]{"רדיו","תוכניות","רשימות השמעה"};

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new RadioFragment();
                case 1:
                    return new ProgramsFragment();
                case 2:
                    return new PlaylistFragment();
            }
            throw new IllegalArgumentException("There are only 3 screens");
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }