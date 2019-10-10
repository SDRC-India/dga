/*
 * Copyright (C) 2015 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.sdrc.dgacg.collect.android.spatial;

/**
 * Created by jnordling on 12/29/15.
 *
 * @author jonnordling@gmail.com
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.preferences.PreferenceKeys;
import org.sdrc.dgacg.collect.android.utilities.ToastUtils;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Locale;

public class MapHelper {
    private static SharedPreferences sharedPreferences;
    public static String[] offilineOverlays;
    private static final String no_folder_key = "None";

    public GoogleMap googleMap;
    public MapView osmMap;

    // GOOGLE MAPS BASEMAPS
    private static final String GOOGLE_MAP_STREETS = "streets";
    private static final String GOOGLE_MAP_SATELLITE = "satellite";
    private static final String GOOGLE_MAP_TERRAIN = "terrain‎";
    private static final String GOOGLE_MAP_HYBRID = "hybrid";

    //OSM MAP BASEMAPS
    private static final String OPENMAP_STREETS = "openmap_streets";
    private static final String OPENMAP_USGS_TOPO = "openmap_usgs_topo";
    private static final String OPENMAP_USGS_SAT = "openmap_usgs_sat";
    private static final String OPENMAP_USGS_IMG = "openmap_usgs_img";
    private static final String OPENMAP_STAMEN_TERRAIN = "openmap_stamen_terrain";
    private static final String OPENMAP_CARTODB_POSITRON = "openmap_cartodb_positron";
    private static final String OPENMAP_CARTODB_DARKMATTER = "openmap_cartodb_darkmatter";
    private int selectedLayer = 0;

    public static String[] geofileTypes = new String[]{".mbtiles", ".kml", ".kmz"};
    private static final String slash = File.separator;

    private TilesOverlay osmTileOverlay;
    private TileOverlay googleTileOverlay;
    private IRegisterReceiver iregisterReceiver;

    private org.sdrc.dgacg.collect.android.spatial.TileSourceFactory tileFactory;


    public MapHelper(Context context, GoogleMap googleMap) {
        this.googleMap = null;
        osmMap = null;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        offilineOverlays = getOfflineLayerList();
        this.googleMap = googleMap;
        tileFactory = new org.sdrc.dgacg.collect.android.spatial.TileSourceFactory(context);
    }

    public MapHelper(Context context, MapView osmMap, IRegisterReceiver iregisterReceiver) {
        googleMap = null;
        this.osmMap = null;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        offilineOverlays = getOfflineLayerList();
        this.iregisterReceiver = iregisterReceiver;
        this.osmMap = osmMap;
        tileFactory = new org.sdrc.dgacg.collect.android.spatial.TileSourceFactory(context);
    }

    private static String getGoogleBasemap() {
        return sharedPreferences.getString(PreferenceKeys.KEY_MAP_BASEMAP, GOOGLE_MAP_STREETS);
    }

    private static String getOsmBasemap() {
        return sharedPreferences.getString(PreferenceKeys.KEY_MAP_BASEMAP, OPENMAP_STREETS);
    }

    public void setBasemap() {
        if (googleMap != null) {
            String basemap = getGoogleBasemap();
            switch (basemap) {
                case GOOGLE_MAP_STREETS:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case GOOGLE_MAP_SATELLITE:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case GOOGLE_MAP_TERRAIN:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case GOOGLE_MAP_HYBRID:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                default:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
            }
        } else {
            //OSMMAP
            String basemap = getOsmBasemap();

            ITileSource tileSource = null;

            switch (basemap) {
                case OPENMAP_USGS_TOPO:
                    tileSource = tileFactory.getUSGSTopo();
                    break;

                case OPENMAP_USGS_SAT:
                    tileSource = tileFactory.getUsgsSat();
                    break;
                    
                case OPENMAP_USGS_IMG:
                    tileSource = tileFactory.getUsgsImg();
                    break;
                    
                case OPENMAP_STAMEN_TERRAIN:
                    tileSource = tileFactory.getStamenTerrain();
                    break;

                case OPENMAP_CARTODB_POSITRON:
                    tileSource = tileFactory.getCartoDbPositron();
                    break;

                case OPENMAP_CARTODB_DARKMATTER:
                    tileSource = tileFactory.getCartoDbDarkMatter();
                    break;

                case OPENMAP_STREETS:
                default:
                    tileSource = TileSourceFactory.MAPNIK;
                    break;
            }

            if (tileSource != null) {
                osmMap.setTileSource(tileSource);
            }
        }

    }

    public static String[] getOfflineLayerList() {
        File[] files = new File(Collect.OFFLINE_LAYERS).listFiles();
        ArrayList<String> results = new ArrayList<String>();
        results.add(no_folder_key);
        for (File f : files) {
            if (f.isDirectory() && !f.isHidden()) {
                results.add(f.getName());
            }
        }

        return results.toArray(new String[0]);
    }

    public void showLayersDialog(final Context context) {
        AlertDialog.Builder layerDialod = new AlertDialog.Builder(context);
        layerDialod.setTitle(context.getString(R.string.select_offline_layer));
        layerDialod.setSingleChoiceItems(offilineOverlays,
                selectedLayer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                if (googleMap != null) {
                                    if (googleTileOverlay != null) {
                                        googleTileOverlay.remove();
                                    }

                                } else {
                                    //OSM
                                    if (osmTileOverlay != null) {
                                        osmMap.getOverlays().remove(osmTileOverlay);
                                        osmMap.invalidate();
                                    }
                                }
                                selectedLayer = item;
                                break;
                            default:
                                File[] spFiles = getFileFromSelectedItem(item);
                                if (spFiles.length == 0) {
                                    break;
                                } else {
                                    File spfile = spFiles[0];

                                    if (isFileFormatSupported(spfile)) {
                                        if (googleMap != null) {
                                            try {
                                                //googleMap.clear();
                                                if (googleTileOverlay != null) {
                                                    googleTileOverlay.remove();
                                                }
                                                TileOverlayOptions opts = new TileOverlayOptions();
                                                GoogleMapsMapBoxOfflineTileProvider provider =
                                                        new GoogleMapsMapBoxOfflineTileProvider(spfile);
                                                opts.tileProvider(provider);
                                                googleTileOverlay = googleMap.addTileOverlay(opts);
                                            } catch (Exception e) {
                                                break;
                                            }
                                        } else {
                                            if (osmTileOverlay != null) {
                                                osmMap.getOverlays().remove(osmTileOverlay);
                                                osmMap.invalidate();
                                            }
                                            osmMap.invalidate();
                                            OsmMBTileProvider mbprovider = new OsmMBTileProvider(
                                                    iregisterReceiver, spfile);
                                            osmTileOverlay = new TilesOverlay(mbprovider, context);
                                            osmTileOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
                                            osmMap.getOverlays().add(0, osmTileOverlay);
                                            osmMap.invalidate();
                                        }
                                        selectedLayer = item;
                                    } else {
                                        ToastUtils.showLongToast(R.string.not_supported_offline_layer_format);
                                    }
                                }
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        layerDialod.show();

    }

    private File[] getFileFromSelectedItem(int item) {
        File directory = new File(Collect.OFFLINE_LAYERS + slash + offilineOverlays[item]);
        return directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return (filename.toLowerCase(Locale.US).endsWith(".mbtiles"));
            }
        });
    }

    // osmdroid doesn't currently support pbf tiles: https://github.com/osmdroid/osmdroid/issues/101
    private boolean isFileFormatSupported(File file) {
        boolean result = true;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("SELECT * FROM metadata where name =?", new String[]{"format"});
        if (cursor != null && cursor.getCount() == 1) {
            try {
                cursor.moveToFirst();
                result = !"pbf".equals(cursor.getString(cursor.getColumnIndex("value")));
            } finally {
                cursor.close();
            }
        }
        return result;
    }

}
