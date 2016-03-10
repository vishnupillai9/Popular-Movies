package me.swiftly.popularmovies;

import android.content.Context;
import android.support.v7.graphics.Palette;

import com.github.florent37.picassopalette.PicassoPalette;

import java.util.HashMap;

/**
 * Created by vishnu on 10/03/16.
 */
public class PaletteHelper {
    public enum Color {
        PRIMARY,
        DARK,
        LIGHT
    }

    public static HashMap<Color, Integer> getColorsFromPalette(Context context, Palette palette) {
        HashMap<Color, Integer> colors = new HashMap<Color, Integer>();

        Integer primary = palette.getVibrantColor(PicassoPalette.Swatch.RGB);
        Integer dark = palette.getDarkVibrantColor(PicassoPalette.Swatch.RGB);
        Integer light = palette.getLightVibrantColor(PicassoPalette.Swatch.RGB);

        if (primary == 0 || dark == 0) {
            primary = palette.getMutedColor(PicassoPalette.Swatch.RGB);
            dark = palette.getDarkMutedColor(PicassoPalette.Swatch.RGB);
        }

        if (light == 0) {
            light = palette.getLightMutedColor(PicassoPalette.Swatch.RGB);
        }

        if (primary == 0 || dark == 0 || light == 0) {
            primary = context.getResources().getColor(R.color.colorPrimary);
            dark = context.getResources().getColor(R.color.colorPrimaryDark);
            light = context.getResources().getColor(R.color.colorAccent);
        }

        colors.put(Color.PRIMARY, primary);
        colors.put(Color.DARK, dark);
        colors.put(Color.LIGHT, light);

        return colors;
    }
}
