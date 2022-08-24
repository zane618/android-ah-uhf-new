package com.beiming.uhf_test.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.beiming.uhf_test.utils.GreenDaoUtil.MeterBeanConverter;
import com.beiming.uhf_test.utils.GreenDaoUtil.PhotoBeanConverter;
import java.util.List;

import com.beiming.uhf_test.bean.MeasBoxBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEAS_BOX_BEAN".
*/
public class MeasBoxBeanDao extends AbstractDao<MeasBoxBean, Long> {

    public static final String TABLENAME = "MEAS_BOX_BEAN";

    /**
     * Properties of entity MeasBoxBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property MeasBoxId = new Property(0, Long.class, "MeasBoxId", true, "_id");
        public final static Property BarCode = new Property(1, String.class, "barCode", false, "BAR_CODE");
        public final static Property MeasAssetNo = new Property(2, String.class, "measAssetNo", false, "MEAS_ASSET_NO");
        public final static Property ScanTime = new Property(3, String.class, "scanTime", false, "SCAN_TIME");
        public final static Property Ts = new Property(4, long.class, "ts", false, "TS");
        public final static Property Gps_X = new Property(5, String.class, "gps_X", false, "GPS__X");
        public final static Property Gps_Y = new Property(6, String.class, "gps_Y", false, "GPS__Y");
        public final static Property Gps_Z = new Property(7, String.class, "gps_Z", false, "GPS__Z");
        public final static Property InstAddr = new Property(8, String.class, "instAddr", false, "INST_ADDR");
        public final static Property InstLoc = new Property(9, String.class, "instLoc", false, "INST_LOC");
        public final static Property Describe = new Property(10, String.class, "describe", false, "DESCRIBE");
        public final static Property TmnlAddr = new Property(11, String.class, "tmnlAddr", false, "TMNL_ADDR");
        public final static Property TgName = new Property(12, String.class, "tgName", false, "TG_NAME");
        public final static Property BoxRows = new Property(13, String.class, "boxRows", false, "BOX_ROWS");
        public final static Property BoxCols = new Property(14, String.class, "boxCols", false, "BOX_COLS");
        public final static Property Note = new Property(15, String.class, "note", false, "NOTE");
        public final static Property IsExsit = new Property(16, boolean.class, "isExsit", false, "IS_EXSIT");
        public final static Property HasQx = new Property(17, String.class, "hasQx", false, "HAS_QX");
        public final static Property QxDetail = new Property(18, String.class, "qxDetail", false, "QX_DETAIL");
        public final static Property FenzhixCode = new Property(19, String.class, "fenzhixCode", false, "FENZHIX_CODE");
        public final static Property Caizhi = new Property(20, String.class, "caizhi", false, "CAIZHI");
        public final static Property Gao = new Property(21, String.class, "gao", false, "GAO");
        public final static Property Kuan = new Property(22, String.class, "kuan", false, "KUAN");
        public final static Property ZsGao = new Property(23, String.class, "zsGao", false, "ZS_GAO");
        public final static Property ZsKuan = new Property(24, String.class, "zsKuan", false, "ZS_KUAN");
        public final static Property ZxGao = new Property(25, String.class, "zxGao", false, "ZX_GAO");
        public final static Property ZxKuan = new Property(26, String.class, "zxKuan", false, "ZX_KUAN");
        public final static Property YsGao = new Property(27, String.class, "ysGao", false, "YS_GAO");
        public final static Property YsKuan = new Property(28, String.class, "ysKuan", false, "YS_KUAN");
        public final static Property YxGao = new Property(29, String.class, "yxGao", false, "YX_GAO");
        public final static Property YxKuan = new Property(30, String.class, "yxKuan", false, "YX_KUAN");
        public final static Property Meters = new Property(31, String.class, "meters", false, "METERS");
        public final static Property BoxImages = new Property(32, String.class, "boxImages", false, "BOX_IMAGES");
    }

    private final MeterBeanConverter metersConverter = new MeterBeanConverter();
    private final PhotoBeanConverter boxImagesConverter = new PhotoBeanConverter();

    public MeasBoxBeanDao(DaoConfig config) {
        super(config);
    }
    
    public MeasBoxBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEAS_BOX_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: MeasBoxId
                "\"BAR_CODE\" TEXT," + // 1: barCode
                "\"MEAS_ASSET_NO\" TEXT," + // 2: measAssetNo
                "\"SCAN_TIME\" TEXT," + // 3: scanTime
                "\"TS\" INTEGER NOT NULL ," + // 4: ts
                "\"GPS__X\" TEXT," + // 5: gps_X
                "\"GPS__Y\" TEXT," + // 6: gps_Y
                "\"GPS__Z\" TEXT," + // 7: gps_Z
                "\"INST_ADDR\" TEXT," + // 8: instAddr
                "\"INST_LOC\" TEXT," + // 9: instLoc
                "\"DESCRIBE\" TEXT," + // 10: describe
                "\"TMNL_ADDR\" TEXT," + // 11: tmnlAddr
                "\"TG_NAME\" TEXT," + // 12: tgName
                "\"BOX_ROWS\" TEXT," + // 13: boxRows
                "\"BOX_COLS\" TEXT," + // 14: boxCols
                "\"NOTE\" TEXT," + // 15: note
                "\"IS_EXSIT\" INTEGER NOT NULL ," + // 16: isExsit
                "\"HAS_QX\" TEXT," + // 17: hasQx
                "\"QX_DETAIL\" TEXT," + // 18: qxDetail
                "\"FENZHIX_CODE\" TEXT," + // 19: fenzhixCode
                "\"CAIZHI\" TEXT," + // 20: caizhi
                "\"GAO\" TEXT," + // 21: gao
                "\"KUAN\" TEXT," + // 22: kuan
                "\"ZS_GAO\" TEXT," + // 23: zsGao
                "\"ZS_KUAN\" TEXT," + // 24: zsKuan
                "\"ZX_GAO\" TEXT," + // 25: zxGao
                "\"ZX_KUAN\" TEXT," + // 26: zxKuan
                "\"YS_GAO\" TEXT," + // 27: ysGao
                "\"YS_KUAN\" TEXT," + // 28: ysKuan
                "\"YX_GAO\" TEXT," + // 29: yxGao
                "\"YX_KUAN\" TEXT," + // 30: yxKuan
                "\"METERS\" TEXT," + // 31: meters
                "\"BOX_IMAGES\" TEXT);"); // 32: boxImages
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEAS_BOX_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MeasBoxBean entity) {
        stmt.clearBindings();
 
        Long MeasBoxId = entity.getMeasBoxId();
        if (MeasBoxId != null) {
            stmt.bindLong(1, MeasBoxId);
        }
 
        String barCode = entity.getBarCode();
        if (barCode != null) {
            stmt.bindString(2, barCode);
        }
 
        String measAssetNo = entity.getMeasAssetNo();
        if (measAssetNo != null) {
            stmt.bindString(3, measAssetNo);
        }
 
        String scanTime = entity.getScanTime();
        if (scanTime != null) {
            stmt.bindString(4, scanTime);
        }
        stmt.bindLong(5, entity.getTs());
 
        String gps_X = entity.getGps_X();
        if (gps_X != null) {
            stmt.bindString(6, gps_X);
        }
 
        String gps_Y = entity.getGps_Y();
        if (gps_Y != null) {
            stmt.bindString(7, gps_Y);
        }
 
        String gps_Z = entity.getGps_Z();
        if (gps_Z != null) {
            stmt.bindString(8, gps_Z);
        }
 
        String instAddr = entity.getInstAddr();
        if (instAddr != null) {
            stmt.bindString(9, instAddr);
        }
 
        String instLoc = entity.getInstLoc();
        if (instLoc != null) {
            stmt.bindString(10, instLoc);
        }
 
        String describe = entity.getDescribe();
        if (describe != null) {
            stmt.bindString(11, describe);
        }
 
        String tmnlAddr = entity.getTmnlAddr();
        if (tmnlAddr != null) {
            stmt.bindString(12, tmnlAddr);
        }
 
        String tgName = entity.getTgName();
        if (tgName != null) {
            stmt.bindString(13, tgName);
        }
 
        String boxRows = entity.getBoxRows();
        if (boxRows != null) {
            stmt.bindString(14, boxRows);
        }
 
        String boxCols = entity.getBoxCols();
        if (boxCols != null) {
            stmt.bindString(15, boxCols);
        }
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(16, note);
        }
        stmt.bindLong(17, entity.getIsExsit() ? 1L: 0L);
 
        String hasQx = entity.getHasQx();
        if (hasQx != null) {
            stmt.bindString(18, hasQx);
        }
 
        String qxDetail = entity.getQxDetail();
        if (qxDetail != null) {
            stmt.bindString(19, qxDetail);
        }
 
        String fenzhixCode = entity.getFenzhixCode();
        if (fenzhixCode != null) {
            stmt.bindString(20, fenzhixCode);
        }
 
        String caizhi = entity.getCaizhi();
        if (caizhi != null) {
            stmt.bindString(21, caizhi);
        }
 
        String gao = entity.getGao();
        if (gao != null) {
            stmt.bindString(22, gao);
        }
 
        String kuan = entity.getKuan();
        if (kuan != null) {
            stmt.bindString(23, kuan);
        }
 
        String zsGao = entity.getZsGao();
        if (zsGao != null) {
            stmt.bindString(24, zsGao);
        }
 
        String zsKuan = entity.getZsKuan();
        if (zsKuan != null) {
            stmt.bindString(25, zsKuan);
        }
 
        String zxGao = entity.getZxGao();
        if (zxGao != null) {
            stmt.bindString(26, zxGao);
        }
 
        String zxKuan = entity.getZxKuan();
        if (zxKuan != null) {
            stmt.bindString(27, zxKuan);
        }
 
        String ysGao = entity.getYsGao();
        if (ysGao != null) {
            stmt.bindString(28, ysGao);
        }
 
        String ysKuan = entity.getYsKuan();
        if (ysKuan != null) {
            stmt.bindString(29, ysKuan);
        }
 
        String yxGao = entity.getYxGao();
        if (yxGao != null) {
            stmt.bindString(30, yxGao);
        }
 
        String yxKuan = entity.getYxKuan();
        if (yxKuan != null) {
            stmt.bindString(31, yxKuan);
        }
 
        List meters = entity.getMeters();
        if (meters != null) {
            stmt.bindString(32, metersConverter.convertToDatabaseValue(meters));
        }
 
        List boxImages = entity.getBoxImages();
        if (boxImages != null) {
            stmt.bindString(33, boxImagesConverter.convertToDatabaseValue(boxImages));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MeasBoxBean entity) {
        stmt.clearBindings();
 
        Long MeasBoxId = entity.getMeasBoxId();
        if (MeasBoxId != null) {
            stmt.bindLong(1, MeasBoxId);
        }
 
        String barCode = entity.getBarCode();
        if (barCode != null) {
            stmt.bindString(2, barCode);
        }
 
        String measAssetNo = entity.getMeasAssetNo();
        if (measAssetNo != null) {
            stmt.bindString(3, measAssetNo);
        }
 
        String scanTime = entity.getScanTime();
        if (scanTime != null) {
            stmt.bindString(4, scanTime);
        }
        stmt.bindLong(5, entity.getTs());
 
        String gps_X = entity.getGps_X();
        if (gps_X != null) {
            stmt.bindString(6, gps_X);
        }
 
        String gps_Y = entity.getGps_Y();
        if (gps_Y != null) {
            stmt.bindString(7, gps_Y);
        }
 
        String gps_Z = entity.getGps_Z();
        if (gps_Z != null) {
            stmt.bindString(8, gps_Z);
        }
 
        String instAddr = entity.getInstAddr();
        if (instAddr != null) {
            stmt.bindString(9, instAddr);
        }
 
        String instLoc = entity.getInstLoc();
        if (instLoc != null) {
            stmt.bindString(10, instLoc);
        }
 
        String describe = entity.getDescribe();
        if (describe != null) {
            stmt.bindString(11, describe);
        }
 
        String tmnlAddr = entity.getTmnlAddr();
        if (tmnlAddr != null) {
            stmt.bindString(12, tmnlAddr);
        }
 
        String tgName = entity.getTgName();
        if (tgName != null) {
            stmt.bindString(13, tgName);
        }
 
        String boxRows = entity.getBoxRows();
        if (boxRows != null) {
            stmt.bindString(14, boxRows);
        }
 
        String boxCols = entity.getBoxCols();
        if (boxCols != null) {
            stmt.bindString(15, boxCols);
        }
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(16, note);
        }
        stmt.bindLong(17, entity.getIsExsit() ? 1L: 0L);
 
        String hasQx = entity.getHasQx();
        if (hasQx != null) {
            stmt.bindString(18, hasQx);
        }
 
        String qxDetail = entity.getQxDetail();
        if (qxDetail != null) {
            stmt.bindString(19, qxDetail);
        }
 
        String fenzhixCode = entity.getFenzhixCode();
        if (fenzhixCode != null) {
            stmt.bindString(20, fenzhixCode);
        }
 
        String caizhi = entity.getCaizhi();
        if (caizhi != null) {
            stmt.bindString(21, caizhi);
        }
 
        String gao = entity.getGao();
        if (gao != null) {
            stmt.bindString(22, gao);
        }
 
        String kuan = entity.getKuan();
        if (kuan != null) {
            stmt.bindString(23, kuan);
        }
 
        String zsGao = entity.getZsGao();
        if (zsGao != null) {
            stmt.bindString(24, zsGao);
        }
 
        String zsKuan = entity.getZsKuan();
        if (zsKuan != null) {
            stmt.bindString(25, zsKuan);
        }
 
        String zxGao = entity.getZxGao();
        if (zxGao != null) {
            stmt.bindString(26, zxGao);
        }
 
        String zxKuan = entity.getZxKuan();
        if (zxKuan != null) {
            stmt.bindString(27, zxKuan);
        }
 
        String ysGao = entity.getYsGao();
        if (ysGao != null) {
            stmt.bindString(28, ysGao);
        }
 
        String ysKuan = entity.getYsKuan();
        if (ysKuan != null) {
            stmt.bindString(29, ysKuan);
        }
 
        String yxGao = entity.getYxGao();
        if (yxGao != null) {
            stmt.bindString(30, yxGao);
        }
 
        String yxKuan = entity.getYxKuan();
        if (yxKuan != null) {
            stmt.bindString(31, yxKuan);
        }
 
        List meters = entity.getMeters();
        if (meters != null) {
            stmt.bindString(32, metersConverter.convertToDatabaseValue(meters));
        }
 
        List boxImages = entity.getBoxImages();
        if (boxImages != null) {
            stmt.bindString(33, boxImagesConverter.convertToDatabaseValue(boxImages));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MeasBoxBean readEntity(Cursor cursor, int offset) {
        MeasBoxBean entity = new MeasBoxBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // MeasBoxId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // barCode
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // measAssetNo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // scanTime
            cursor.getLong(offset + 4), // ts
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // gps_X
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // gps_Y
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // gps_Z
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // instAddr
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // instLoc
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // describe
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // tmnlAddr
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // tgName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // boxRows
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // boxCols
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // note
            cursor.getShort(offset + 16) != 0, // isExsit
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // hasQx
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // qxDetail
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // fenzhixCode
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // caizhi
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // gao
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // kuan
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // zsGao
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // zsKuan
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // zxGao
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // zxKuan
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // ysGao
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // ysKuan
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // yxGao
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // yxKuan
            cursor.isNull(offset + 31) ? null : metersConverter.convertToEntityProperty(cursor.getString(offset + 31)), // meters
            cursor.isNull(offset + 32) ? null : boxImagesConverter.convertToEntityProperty(cursor.getString(offset + 32)) // boxImages
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MeasBoxBean entity, int offset) {
        entity.setMeasBoxId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBarCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMeasAssetNo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setScanTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTs(cursor.getLong(offset + 4));
        entity.setGps_X(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGps_Y(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setGps_Z(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setInstAddr(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setInstLoc(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setDescribe(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTmnlAddr(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setTgName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setBoxRows(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setBoxCols(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setNote(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setIsExsit(cursor.getShort(offset + 16) != 0);
        entity.setHasQx(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setQxDetail(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setFenzhixCode(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setCaizhi(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setGao(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setKuan(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setZsGao(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setZsKuan(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setZxGao(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setZxKuan(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setYsGao(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setYsKuan(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setYxGao(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setYxKuan(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setMeters(cursor.isNull(offset + 31) ? null : metersConverter.convertToEntityProperty(cursor.getString(offset + 31)));
        entity.setBoxImages(cursor.isNull(offset + 32) ? null : boxImagesConverter.convertToEntityProperty(cursor.getString(offset + 32)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MeasBoxBean entity, long rowId) {
        entity.setMeasBoxId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MeasBoxBean entity) {
        if(entity != null) {
            return entity.getMeasBoxId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MeasBoxBean entity) {
        return entity.getMeasBoxId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
