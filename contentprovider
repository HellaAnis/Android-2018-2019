package com.example.anis.tp06;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class MyContentProvider extends ContentProvider {
 BaseGeo helper;
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id=0;
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        String path="" ;


        try {
            switch (code) {
                case 5:
                    id = db.delete("stat",selection,selectionArgs);
                    id = db.delete("geo",selection,selectionArgs);

                    break;

                default:
                    throw new UnsupportedOperationException("Not yet implemented");
            }
        }catch (Exception e){e.getMessage();}

        return id;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    long id=0;
            SQLiteDatabase db = helper.getWritableDatabase();
            int code = matcher.match(uri);
        String path="" ;


        try {
            switch (code) {
            case CODE_GEO:
                id = db.insert("geo", null, values);
                path = "geo";
                break;
            case STAT_GEO:
                id = db.insert(helper.TABLE_STAT, null, values);
                path = "stat";
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        }catch (Exception e){e.getMessage();}

        Uri.Builder builder = (new Uri.Builder())
                .authority(authority)
                .appendPath(path);
        /* retourner Uri dans ContentResolver */
        return ContentUris.appendId(builder, id).build();
    }

    @Override
    public boolean onCreate() {

        helper = BaseGeo.getInstance(getContext());

        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = helper.getReadableDatabase();
        int code = matcher.match(uri);

        Log.i("tp6pro","code="+code);
         //on cree un switch qui utilse comme attribut notre URI Matcher
        Cursor cursor;
        String []collumns ={"rowid as _id",helper.COLONNE_PAYS,helper.COLONNE_CAPITALE};
        switch (code) {
            case 1:
                cursor = db.query("geo", collumns, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case 2:
                cursor = db.query("stat", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case 3:
                //uri.getPathSegments recupere une liste qui 2 attribut infopays-0-/lepays -1-
                List<String> l= uri.getPathSegments();
                //on récuper le 1 element le pays quand cherche
                String pays=l.get(1);
                //faire un string tableau qui contien les args on doit faire une requet selection sur eux "where === ?"
                String [] selargs ={pays};
                Log.d("tp6pro","pays="+pays);

                //on cree notre cursor quit contient notre requete
                cursor = db.query("geo", null, "pays = ?",
                        selargs, null, null, sortOrder);
                break;
            case 4:
                //uri.getPathSegments recupere une liste qui 2 attribut infopays-0-/lepays -1-
                List<String> liste= uri.getPathSegments();
                Log.i("tp6pro","proooo");

                //on récuper le 1 element le pays quand cherche
                String pays1=liste.get(1);
                Log.i("tp6pro","proooo1");

                String annee= liste.get(2);
                Log.i("tp6pro","proooo2");

                //faire un string tableau qui contien les args on doit faire une requet selection sur eux "where === ?"
                String [] selargs1 ={pays1,annee};
                Log.i("tp6pro","proooo3");

                Log.i("tp6pro","pays="+pays1);

                //on cree notre cursor quit contient notre requete
                Log.i("tp6pro","proooo4");
                String whereClause = "geo.pays = ? and stat.annee=? and paye.geo=stat.pays ";
                String [] tables ={"geo","stat"};
// la meme choose mais on fait en plus la jointure
                String MY_QUERY = "SELECT * FROM stat s INNER JOIN geo g ON s.pays=g.pays WHERE s.pays=? and s.annee=?";

                cursor=db.rawQuery(MY_QUERY,selargs1);
                Log.i("tp6pro","proooo5");

                break;


            default:
                Log.i("Uri provider =", uri.toString());
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return cursor;
    }



        // TODO: Implement this to handle query requests from clients.


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private static String authority = "fr.votrenom.geographie";

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CODE_GEO=1;
    private static final int STAT_GEO=2;


    static {

        matcher.addURI(authority,"geo",CODE_GEO);
        matcher.addURI(authority,"stat",STAT_GEO);
        matcher.addURI(authority,"infopays/*",3);
        matcher.addURI(authority,"uneligne/*/#",4);
        matcher.addURI(authority,"supprimepays/*",5);

        //votre_authority/supprimepays/*








    }
}
