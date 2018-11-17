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
/////////////////////////////////////////////////////////////////////////////
package com.example.anis.tp06_suite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class AccesDonnees  {

    ContentResolver cr;
     public AccesDonnees(Context context){
         //initialiser le contentresolver
         cr=context.getContentResolver();
         }

    public final static int VERSION = 9;
    public final static String DB_NAME = "base_geo";
    public final static String TABLE_GEO = "geo";
    public final static String COLONNE_PAYS = "pays";
    public final static String COLONNE_CAPITALE = "capitale";
    public final static String COLONNE_CONTINENT = "continent";
    public final static String COLONNE_SUPERFICIE = "superficie";



    public final static String TABLE_STAT = "stat";
    public final static String COLONNE_ANNEE = "annee";
    public final static String COLONNE_POPULATION = "population";

    public void ajoutPays(String pays,String capitale,String contient, int superficie){
        ContentValues cv = new ContentValues();
        cv.put(COLONNE_PAYS,pays);
        cv.put(COLONNE_CAPITALE,capitale);
        cv.put(COLONNE_CONTINENT,contient);
        cv.put(COLONNE_SUPERFICIE,superficie);
        Uri.Builder builder = (new Uri.Builder()).scheme("content")
                .authority("fr.votrenom.geographie")
                .appendPath(TABLE_GEO);
       Uri uri= builder.build();

        cr.insert(uri,cv);


    }

    public void ajoutStat(String pays,int anne, int population){

        ContentValues cv=new ContentValues();
        cv.put(COLONNE_PAYS,pays);
        cv.put(COLONNE_ANNEE,anne);
        cv.put(COLONNE_POPULATION,population);

        Uri.Builder builder = (new Uri.Builder().scheme("content")).authority("fr.votrenom.geographie").appendPath("stat");

        cr.insert(builder.build(),cv);

        }


    public void init() {
        ajoutPays("France", "Paris", "Europe", 551500);
        ajoutStat("France", 2005, 61233900);
        ajoutStat("France", 2010, 63026740);
        ajoutStat("France", 2015, 64457201);
        ajoutPays("Togo", "Lomé", "Afrique", 56785);
        ajoutStat("Togo", 1981, 2719567);
        ajoutStat("Togo", 2005, 5683268);
        ajoutStat("Togo", 2010, 6502952);
        ajoutStat("Togo", 2015, 7416802);
        ajoutPays("Bhoutan", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan", 2005, 656639);
        ajoutStat("Bhoutan", 2010, 727641);
        ajoutStat("Bhoutan", 2015, 787836);
        ajoutPays("France1", "Paris", "Europe", 551500);
        ajoutStat("France1", 2005, 61233900);
        ajoutStat("France1", 2010, 63026740);
        ajoutStat("France1", 2015, 64457201);
        ajoutPays("Togo1", "Lomé", "Afrique", 56785);
        ajoutStat("Togo1", 1981, 2719567);
        ajoutStat("Togo1", 2005, 5683268);
        ajoutStat("Togo1", 2010, 6502952);
        ajoutStat("Togo1", 2015, 7416802);
        ajoutPays("Bhoutan1", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan1", 2005, 656639);
        ajoutStat("Bhoutan1", 2010, 727641);
        ajoutStat("Bhoutan1", 2015, 787836);
        ajoutPays("France2", "Paris", "Europe", 551500);
        ajoutStat("France2", 2005, 61233900);
        ajoutStat("France2", 2010, 63026740);
        ajoutStat("France2", 2015, 64457201);
        ajoutPays("Togo2", "Lomé", "Afrique", 56785);
        ajoutStat("Togo2", 1981, 2719567);
        ajoutStat("Togo2", 2005, 5683268);
        ajoutStat("Togo2", 2010, 6502952);
        ajoutStat("Togo2", 2015, 7416802);
        ajoutPays("Bhoutan2", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan2", 2005, 656639);
        ajoutStat("Bhoutan2", 2010, 727641);
        ajoutStat("Bhoutan2", 2015, 787836);
        ajoutPays("France3", "Paris", "Europe", 551500);
        ajoutStat("France3", 2005, 61233900);
        ajoutStat("France3", 2010, 63026740);
        ajoutStat("France3", 2015, 64457201);
        ajoutPays("Togo3", "Lomé", "Afrique", 56785);
        ajoutStat("Togo3", 1981, 2719567);
        ajoutStat("Togo3", 2005, 5683268);
        ajoutStat("Togo3", 2010, 6502952);
        ajoutStat("Togo3", 2015, 7416802);
        ajoutPays("Bhoutan3", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan3", 2005, 656639);
        ajoutStat("Bhoutan3", 2010, 727641);
        ajoutStat("Bhoutan3", 2015, 787836);
        ajoutPays("France4", "Paris", "Europe", 551500);
        ajoutStat("France4", 2005, 61233900);
        ajoutStat("France4", 2010, 63026740);
        ajoutStat("France4", 2015, 64457201);
        ajoutPays("Togo4", "Lomé", "Afrique", 56785);
        ajoutStat("Togo4", 1981, 2719567);
        ajoutStat("Togo4", 2005, 5683268);
        ajoutStat("Togo4", 2010, 6502952);
        ajoutStat("Togo4", 2015, 7416802);
        ajoutPays("Bhoutan4", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan4", 2005, 656639);
        ajoutStat("Bhoutan4", 2010, 727641);
        ajoutStat("Bhoutan4", 2015, 787836);
        ajoutPays("France5", "Paris", "Europe", 551500);
        ajoutStat("France5", 2005, 61233900);
        ajoutStat("France5", 2010, 63026740);
        ajoutStat("France5", 2015, 64457201);
        ajoutPays("Togo5", "Lomé", "Afrique", 56785);
        ajoutStat("Togo5", 1981, 2719567);
        ajoutStat("Togo5", 2005, 5683268);
        ajoutStat("Togo5", 2010, 6502952);
        ajoutStat("Togo5", 2015, 7416802);
        ajoutPays("Bhoutan5", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan5", 2005, 656639);
        ajoutStat("Bhoutan5", 2010, 727641);
        ajoutStat("Bhoutan5", 2015, 787836);
        ajoutPays("France6", "Paris", "Europe", 551500);
        ajoutStat("France6", 2005, 61233900);
        ajoutStat("France6", 2010, 63026740);
        ajoutStat("France6", 2015, 64457201);
        ajoutPays("Togo6", "Lomé", "Afrique", 56785);
        ajoutStat("Togo6", 1981, 2719567);
        ajoutStat("Togo6", 2005, 5683268);
        ajoutStat("Togo6", 2010, 6502952);
        ajoutStat("Togo6", 2015, 7416802);
        ajoutPays("Bhoutan6", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan6", 2005, 656639);
        ajoutStat("Bhoutan6", 2010, 727641);
        ajoutStat("Bhoutan6", 2015, 787836);
        ajoutPays("France7", "Paris", "Europe", 551500);
        ajoutStat("France7", 2005, 61233900);
        ajoutStat("France7", 2010, 63026740);
        ajoutStat("France7", 2015, 64457201);
        ajoutPays("Togo7", "Lomé", "Afrique", 56785);
        ajoutStat("Togo7", 1981, 2719567);
        ajoutStat("Togo7", 2005, 5683268);
        ajoutStat("Togo7", 2010, 6502952);
        ajoutStat("Togo7", 2015, 7416802);
        ajoutPays("Bhoutan7", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan7", 2005, 656639);
        ajoutStat("Bhoutan7", 2010, 727641);
        ajoutStat("Bhoutan7", 2015, 787836);
        ajoutPays("France8", "Paris", "Europe", 551500);
        ajoutStat("France8", 2005, 61233900);
        ajoutStat("France8", 2010, 63026740);
        ajoutStat("France8", 2015, 64457201);
        ajoutPays("Togo8", "Lomé", "Afrique", 56785);
        ajoutStat("Togo8", 1981, 2719567);
        ajoutStat("Togo8", 2005, 5683268);
        ajoutStat("Togo8", 2010, 6502952);
        ajoutStat("Togo8", 2015, 7416802);
        ajoutPays("Bhoutan8", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan8", 2005, 656639);
        ajoutStat("Bhoutan8", 2010, 727641);
        ajoutStat("Bhoutan8", 2015, 787836);
        ajoutPays("France11", "Paris", "Europe", 551500);
        ajoutStat("France11", 2005, 61233900);
        ajoutStat("France11", 2010, 63026740);
        ajoutStat("France11", 2015, 64457201);
        ajoutPays("Togo11", "Lomé", "Afrique", 56785);
        ajoutStat("Togo11", 1981, 2719567);
        ajoutStat("Togo11", 2005, 5683268);
        ajoutStat("Togo11", 2010, 6502952);
        ajoutStat("Togo11", 2015, 7416802);
        ajoutPays("Bhoutan11", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan11", 2005, 656639);
        ajoutStat("Bhoutan11", 2010, 727641);
        ajoutStat("Bhoutan11", 2015, 787836);
        ajoutPays("France12", "Paris", "Europe", 551500);
        ajoutStat("France12", 2005, 61233900);
        ajoutStat("France12", 2010, 63026740);
        ajoutStat("France12", 2015, 64457201);
        ajoutPays("Togo12", "Lomé", "Afrique", 56785);
        ajoutStat("Togo12", 1981, 2719567);
        ajoutStat("Togo12", 2005, 5683268);
        ajoutStat("Togo12", 2010, 6502952);
        ajoutStat("Togo12", 2015, 7416802);
        ajoutPays("Bhoutan12", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan12", 2005, 656639);
        ajoutStat("Bhoutan12", 2010, 727641);
        ajoutStat("Bhoutan12", 2015, 787836);
        ajoutPays("France13", "Paris", "Europe", 551500);
        ajoutStat("France13", 2005, 61233900);
        ajoutStat("France13", 2010, 63026740);
        ajoutStat("France13", 2015, 64457201);
        ajoutPays("Togo13", "Lomé", "Afrique", 56785);
        ajoutStat("Togo13", 1981, 2719567);
        ajoutStat("Togo13", 2005, 5683268);
        ajoutStat("Togo13", 2010, 6502952);
        ajoutStat("Togo13", 2015, 7416802);
        ajoutPays("Bhoutan13", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan13", 2005, 656639);
        ajoutStat("Bhoutan13", 2010, 727641);
        ajoutStat("Bhoutan13", 2015, 787836);
        ajoutPays("France14", "Paris", "Europe", 551500);
        ajoutStat("France14", 2005, 61233900);
        ajoutStat("France14", 2010, 63026740);
        ajoutStat("France14", 2015, 64457201);
        ajoutPays("Togo14", "Lomé", "Afrique", 56785);
        ajoutStat("Togo14", 1981, 2719567);
        ajoutStat("Togo14", 2005, 5683268);
        ajoutStat("Togo14", 2010, 6502952);
        ajoutStat("Togo14", 2015, 7416802);
        ajoutPays("Bhoutan14", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan14", 2005, 656639);
        ajoutStat("Bhoutan14", 2010, 727641);
        ajoutStat("Bhoutan14", 2015, 787836);
        ajoutPays("France15", "Paris", "Europe", 551500);
        ajoutStat("France15", 2005, 61233900);
        ajoutStat("France15", 2010, 63026740);
        ajoutStat("France15", 2015, 64457201);
        ajoutPays("Togo15", "Lomé", "Afrique", 56785);
        ajoutStat("Togo15", 1981, 2719567);
        ajoutStat("Togo15", 2005, 5683268);
        ajoutStat("Togo15", 2010, 6502952);
        ajoutStat("Togo15", 2015, 7416802);
        ajoutPays("Bhoutan15", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan15", 2005, 656639);
        ajoutStat("Bhoutan15", 2010, 727641);
        ajoutStat("Bhoutan15", 2015, 787836);
        ajoutPays("France16", "Paris", "Europe", 551500);
        ajoutStat("France16", 2005, 61233900);
        ajoutStat("France16", 2010, 63026740);
        ajoutStat("France16", 2015, 64457201);
        ajoutPays("Togo16", "Lomé", "Afrique", 56785);
        ajoutStat("Togo16", 1981, 2719567);
        ajoutStat("Togo16", 2005, 5683268);
        ajoutStat("Togo16", 2010, 6502952);
        ajoutStat("Togo16", 2015, 7416802);
        ajoutPays("Bhoutan16", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan16", 2005, 656639);
        ajoutStat("Bhoutan16", 2010, 727641);
        ajoutStat("Bhoutan16", 2015, 787836);
        ajoutPays("France17", "Paris", "Europe", 551500);
        ajoutStat("France17", 2005, 61233900);
        ajoutStat("France17", 2010, 63026740);
        ajoutStat("France17", 2015, 64457201);
        ajoutPays("Togo17", "Lomé", "Afrique", 56785);
        ajoutStat("Togo17", 1981, 2719567);
        ajoutStat("Togo17", 2005, 5683268);
        ajoutStat("Togo17", 2010, 6502952);
        ajoutStat("Togo17", 2015, 7416802);
        ajoutPays("Bhoutan17", "Thimphou", "Asie", 38394);
        ajoutStat("Bhoutan17", 2005, 656639);
        ajoutStat("Bhoutan17", 2010, 727641);
        ajoutStat("Bhoutan17", 2015, 787836);
    }

    public int surface(String pays){
        //On fait une fonction suraface qui prend comme attribut un pays et fait une recherche dans la base 'faire selection
        //sur se pays ' puis retourn sa surface .
        Log.i("tp6_suit","arriiiiive ");

        //d'abord on doit creé le builder "apreé faire un uri matcher pour infos pays "
        //donc on donne l'authority / ---> et on fait appendpath pour faire /.../...../../ et aà la fin on donne la variable pays
        Uri.Builder builder = (new Uri.Builder().scheme("content")).authority("fr.votrenom.geographie")
                .appendPath("infopays").appendPath(pays);
        Log.i("tp6_suit","arriiiiive 1");
        // on fait build de notre URI
      Uri u=  builder.build();
        Log.i("tp6_suit","arriiiiive 2");
        // on cree un cursor utilisant le query du content provider
        //on donne comme args 1:Uri
        //le autres  null puisque on veut tout recuper
       Cursor c= cr.query(u,null,null,null,null);
        Log.i("tp6_suit","arriiiiive 3");
        // on deplace directement dans la premier ligne
        c. moveToFirst();
        Log.i("tp6_suit","arriiiiive 4");

        Log.i("tp6_suit","superficie="+c.getInt(c.getColumnIndex("superficie")));
        //on fait ub GetColumnIndex pour recuperer l'indice de la superficie
        //on fait getInt pour recuprer le contenu dur la colonne de l'indice
        return  c.getInt(c.getColumnIndex("superficie"));

    }

    public int densite(String pays,String annee1){


            //On fait une fonction suraface qui prend comme attribut un pays et fait une recherche dans la base 'faire selection
            //sur se pays ' puis retourn sa surface .
            Log.i("tp6_suit","arriiiiive ");

            //d'abord on doit creé le builder "apreé faire un uri matcher pour infos pays "
            //donc on donne l'authority / ---> et on fait appendpath pour faire /.../...../../ et aà la fin on donne la variable pays
            Uri.Builder builder = (new Uri.Builder().scheme("content")).authority("fr.votrenom.geographie")
                    .appendPath("uneligne").appendPath(pays).appendPath(annee1);
            Log.i("tp6_suit","arriiiiive 1");
            // on fait build de notre URI
            Uri u1=  builder.build();
        Log.i("tp6_suit",u1.toString());
            Log.i("tp6_suit","arriiiiive 2");
            // on cree un cursor utilisant le query du content provider
            //on donne comme args 1:Uri
            //le autres  null puisque on veut tout recuper
            Cursor c= cr.query(u1,null,null,null,null);
            Log.i("tp6_suit","arriiiiive 3");
            // on deplace directement dans la premier ligne
            c. moveToFirst();
            Log.i("tp6_suit","arriiiiive 4");

            Log.i("tp6_suit","superficie="+c.getInt(c.getColumnIndex("population")));
            //on fait ub GetColumnIndex pour recuperer l'indice de la superficie
            //on fait getInt pour recuprer le contenu dur la colonne de l'indice
            return  c.getInt(c.getColumnIndex("population"));
    }

    public Cursor spinner(){

        Uri.Builder builder = (new Uri.Builder()).scheme("content")
                .authority("fr.votrenom.geographie")
                .appendPath(TABLE_GEO);
        Uri uri= builder.build();
        String []collumns ={"pays"};

        Cursor c= cr.query(uri,null,null,null,null);
    return c;

    }

    public  int supprimer(String pays){

        Uri.Builder builder = (new Uri.Builder()).scheme("content")
                .authority("fr.votrenom.geographie")
                .appendPath("supprimepays").appendPath(pays);
        Uri uri= builder.build();
        String []collumns ={pays};

        int c= cr.delete(uri,"pays=?",collumns);

        return c;

    }



}

