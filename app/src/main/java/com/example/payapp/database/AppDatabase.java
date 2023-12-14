package com.example.payapp.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.payapp.R;

@Database(entities = {User.class, StoreItem.class, Receipt.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ReceiptDao receiptDao();
    public abstract UserDao userDao();
    public abstract StoreItemDao storeItemDao();

    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "my_database")
                            .addCallback(roomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        Log.d("Bastian", "Database good!");
        return instance;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);



            new Thread(() -> {
                UserDao userDao = instance.userDao();
                StoreItemDao storeItemDao = instance.storeItemDao();

                // Default items
                StoreItem beer = new StoreItem();
                beer.itemName = "Beer";
                beer.price = 7.0;
                beer.image = R.drawable.beer;
                storeItemDao.insert(beer);

                StoreItem soda = new StoreItem();
                soda.itemName = "Soda";
                soda.price = 6.0;
                soda.image = R.drawable.soda;
                storeItemDao.insert(soda);

                // Default users
                User user1 = new User();
                user1.name = "Issue";
                user1.private_seed = "SCHZGSIVFHGPXEFVCVUANVJBIKG3DBA3KLXBRJH6GSMHCBYCTISUYRAJ";
                user1.public_seed = "GDJOSFEOXHL7H4GLETNBECNGIFYY2WXEIYCG3SM6UK6WRDVFPWJVBYF7";
                user1.password = "test";
                userDao.insert(user1);

                User user2 = new User();
                user2.name = "Dist";
                user2.private_seed = "SA6CSJAR2JNK7XP2MCPTG3QM7URQMLAHKSNCDHBVGHA7TYVEW7AH2S5M";
                user2.public_seed = "GDGGD6K47JPRFHLBWVX3XDEGOX55FKVMZQTNKX7O4TDIRMDHFTPIOX43";
                user2.password = "test";
                userDao.insert(user2);

                User user3 = new User();
                user3.name = "Customer1";
                user3.private_seed = "SB5QLXIF2CIMGBLTTNKLDZEPYKO3OWVNSNEQFSS3HQY3MAP2Z2XXROQK";
                user3.public_seed = "GDDNAKEVVWL27JRRQOD4KFDGJLQZ4HEA6WJQPEESVIR43X7OCHOWCGK2";
                user3.password = "test";
                userDao.insert(user3);

                User user4 = new User();
                user4.name = "Customer2";
                user4.private_seed = "SDUAMYBOTBXDE7DMEDDWEGKFDGTRX2F3RSELE5ZZJ4TNSLVKPHGYFIJH";
                user4.public_seed = "GCKOD3FU75R2ZOVZUNJKAC5QNNJPEGXMYPEYSJRC6OATJO7EGX6SN3UM";
                user4.password = "test";
                userDao.insert(user4);
            }).start();
        }
    };
}
