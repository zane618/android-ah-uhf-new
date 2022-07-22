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
        public final static Property Gps_X = new Property(4, String.class, "gps_X", false, "GPS__X");
        public final static Property Gps_Y = new Property(5, String.class, "gps_Y", false, "GPS__Y");
        public final static Property Gps_Z = new Property(6, String.class, "gps_Z", false, "GPS__Z");
        public final static Property InstAddr = new Property(7, String.class, "instAddr", false, "INST_ADDR");
        public final static Property InstLoc = new Property(8, String.class, "instLoc", false, "INST_LOC");
        public final static Property Describe = new Property(9, String.class, "describe", false, "DESCRIBE");
        public final static Property TmnlAddr = new Property(10, String.class, "tmnlAddr", false, "TMNL_ADDR");
        public final static Property TgName = new Property(11, String.class, "tgName", false, "TG_NAME");
        public final static Property BoxRows = new Property(12, String.class, "boxRows", false, "BOX_ROWS");
        public final static Property BoxCols = new Property(13, String.class, "boxCols", false, "BOX_COLS");
        public final static Property Note = new Property(14, String.class, "note", false, "NOTE");
        public final static Property IsExsit = new Property(15, boolean.class, "isExsit", false, "IS_EXSIT");
        public final static Property HasQx = new Property(16, String.class, "hasQx", false, "HAS_QX");
        public final static Property QxJiaolian = new Property(17, String.class, "qxJiaolian", false, "QX_JIAOLIAN");
        public final static Property QxLaogu = new Property(18, String.class, "qxLaogu", false, "QX_LAOGU");
        public final static Property QxSuo = new Property(19, String.class, "qxSuo", false, "QX_SUO");
        public final static Property QxZawu = new Property(20, String.class, "qxZawu", false, "QX_ZAWU");
        public final static Property Caizhi = new Property(21, String.class, "caizhi", false, "CAIZHI");
        public final static Property Chang = new Property(22, String.class, "chang", false, "CHANG");
        public final static Property Kuan = new Property(23, String.class, "kuan", false, "KUAN");
        public final static Property ZsGao = new Property(24, String.class, "zsGao", false, "ZS_GAO");
        public final static Property ZsKuan = new Property(25, String.class, "zsKuan", false, "ZS_KUAN");
        public final static Property ZxGao = new Property(26, String.class, "zxGao", false, "ZX_GAO");
        public final static Property ZxKuan = new Property(27, String.class, "zxKuan", false, "ZX_KUAN");
        public final static Property YsGao = new Property(28, String.class, "ysGao", false, "YS_GAO");
        public final static Property YsKuan = new Property(29, String.class, "ysKuan", false, "YS_KUAN");
        public final static Property YxGao = new Property(30, String.class, "yxGao", false, "YX_GAO");
        public final static Property YxKuan = new Property(31, String.class, "yxKuan", false, "YX_KUAN");
        public final static Property Meters = new Property(32, String.class, "meters", false, "METERS");
        public final static Property BoxImages = new Property(33, String.class, "boxImages", false, "BOX_IMAGES");
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
                "\"GPS__X\" TEXT," + // 4: gps_X
                "\"GPS__Y\" TEXT," + // 5: gps_Y
                "\"GPS__Z\" TEXT," + // 6: gps_Z
                "\"INST_ADDR\" TEXT," + // 7: instAddr
                "\"INST_LOC\" TEXT," + // 8: instLoc
                "\"DESCRIBE\" TEXT," + // 9: describe
                "\"TMNL_ADDR\" TEXT," + // 10: tmnlAddr
                "\"TG_NAME\" TEXT," + // 11: tgName
                "\"BOX_ROWS\" TEXT," + // 12: boxRows
                "\"BOX_COLS\" TEXT," + // 13: boxCols
                "\"NOTE\" TEXT," + // 14: note
                "\"IS_EXSIT\" INTEGER NOT NULL ," + // 15: isExsit
                "\"HAS_QX\" TEXT," + // 16: hasQx
                "\"QX_JIAOLIAN\" TEXT," + // 17: qxJiaolian
                "\"QX_LAOGU\" TEXT," + // 18: qxLaogu
                "\"QX_SUO\" TEXT," + // 19: qxSuo
                "\"QX_ZAWU\" TEXT," + // 20: qxZawu
                "\"CAIZHI\" TEXT," + // 21: caizhi
                "\"CHANG\" TEXT," + // 22: chang
                "\"KUAN\" TEXT," + // 23: kuan
                "\"ZS_GAO\" TEXT," + // 24: zsGao
                "\"ZS_KUAN\" TEXT," + // 25: zsKuan
                "\"ZX_GAO\" TEXT," + // 26: zxGao
                "\"ZX_KUAN\" TEXT," + // 27: zxKuan
                "\"YS_GAO\" TEXT," + // 28: ysGao
                "\"YS_KUAN\" TEXT," + // 29: ysKuan
                "\"YX_GAO\" TEXT," + // 30: yxGao
                "\"YX_KUAN\" TEXT," + // 31: yxKuan
                "\"METERS\" TEXT," + // 32: meters
                "\"BOX_IMAGES\" TEXT);"); // 33: boxImages
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
 
        String gps_X = entity.getGps_X();
        if (gps_X != null) {
            stmt.bindString(5, gps_X);
        }
 
        String gps_Y = entity.getGps_Y();
        if (gps_Y != null) {
            stmt.bindString(6, gps_Y);
        }
 
        String gps_Z = entity.getGps_Z();
        if (gps_Z != null) {
            stmt.bindString(7, gps_Z);
        }
 
        String instAddr = entity.getInstAddr();
        if (instAddr != null) {
            stmt.bindString(8, instAddr);
        }
 
        String instLoc = entity.getInstLoc();
        if (instLoc != null) {
            stmt.bindString(9, instLoc);
        }
 
        String describe = entity.getDescribe();
        if (describe != null) {
            stmt.bindString(10, describe);
        }
 
        String tmnlAddr = entity.getTmnlAddr();
        if (tmnlAddr != null) {
            stmt.bindString(11, tmnlAddr);
        }
 
        String tgName = entity.getTgName();
        if (tgName != null) {
            stmt.bindString(12, tgName);
        }
 
        String boxRows = entity.getBoxRows();
        if (boxRows != null) {
            stmt.bindString(13, boxRows);
        }
 
        String boxCols = entity.getBoxCols();
        if (boxCols != null) {
            stmt.bindString(14, boxCols);
        }
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(15, note);
        }
        stmt.bindLong(16, entity.getIsExsit() ? 1L: 0L);
 
        String hasQx = entity.getHasQx();
        if (hasQx != null) {
            stmt.bindString(17, hasQx);
        }
 
        String qxJiaolian = entity.getQxJiaolian();
        if (qxJiaolian != null) {
            stmt.bindString(18, qxJiaolian);
        }
 
        String qxLaogu = entity.getQxLaogu();
        if (qxLaogu != null) {
            stmt.bindString(19, qxLaogu);
        }
 
        String qxSuo = entity.getQxSuo();
        if (qxSuo != null) {
            stmt.bindString(20, qxSuo);
        }
 
        String qxZawu = entity.getQxZawu();
        if (qxZawu != null) {
            stmt.bindString(21, qxZawu);
        }
 
        String caizhi = entity.getCaizhi();
        if (caizhi != null) {
            stmt.bindString(22, caizhi);
        }
 
        String chang = entity.getChang();
        if (chang != null) {
            stmt.bindString(23, chang);
        }
 
        String kuan = entity.getKuan();
        if (kuan != null) {
            stmt.bindString(24, kuan);
        }
 
        String zsGao = entity.getZsGao();
        if (zsGao != null) {
            stmt.bindString(25, zsGao);
        }
 
        String zsKuan = entity.getZsKuan();
        if (zsKuan != null) {
            stmt.bindString(26, zsKuan);
        }
 
        String zxGao = entity.getZxGao();
        if (zxGao != null) {
            stmt.bindString(27, zxGao);
        }
 
        String zxKuan = entity.getZxKuan();
        if (zxKuan != null) {
            stmt.bindString(28, zxKuan);
        }
 
        String ysGao = entity.getYsGao();
        if (ysGao != null) {
            stmt.bindString(29, ysGao);
        }
 
        String ysKuan = entity.getYsKuan();
        if (ysKuan != null) {
            stmt.bindString(30, ysKuan);
        }
 
        String yxGao = entity.getYxGao();
        if (yxGao != null) {
            stmt.bindString(31, yxGao);
        }
 
        String yxKuan = entity.getYxKuan();
        if (yxKuan != null) {
            stmt.bindString(32, yxKuan);
        }
 
        List meters = entity.getMeters();
        if (meters != null) {
            stmt.bindString(33, metersConverter.convertToDatabaseValue(meters));
        }
 
        List boxImages = entity.getBoxImages();
        if (boxImages != null) {
            stmt.bindString(34, boxImagesConverter.convertToDatabaseValue(boxImages));
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
 
        String gps_X = entity.getGps_X();
        if (gps_X != null) {
            stmt.bindString(5, gps_X);
        }
 
        String gps_Y = entity.getGps_Y();
        if (gps_Y != null) {
            stmt.bindString(6, gps_Y);
        }
 
        String gps_Z = entity.getGps_Z();
        if (gps_Z != null) {
            stmt.bindString(7, gps_Z);
        }
 
        String instAddr = entity.getInstAddr();
        if (instAddr != null) {
            stmt.bindString(8, instAddr);
        }
 
        String instLoc = entity.getInstLoc();
        if (instLoc != null) {
            stmt.bindString(9, instLoc);
        }
 
        String describe = entity.getDescribe();
        if (describe != null) {
            stmt.bindString(10, describe);
        }
 
        String tmnlAddr = entity.getTmnlAddr();
        if (tmnlAddr != null) {
            stmt.bindString(11, tmnlAddr);
        }
 
        String tgName = entity.getTgName();
        if (tgName != null) {
            stmt.bindString(12, tgName);
        }
 
        String boxRows = entity.getBoxRows();
        if (boxRows != null) {
            stmt.bindString(13, boxRows);
        }
 
        String boxCols = entity.getBoxCols();
        if (boxCols != null) {
            stmt.bindString(14, boxCols);
        }
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(15, note);
        }
        stmt.bindLong(16, entity.getIsExsit() ? 1L: 0L);
 
        String hasQx = entity.getHasQx();
        if (hasQx != null) {
            stmt.bindString(17, hasQx);
        }
 
        String qxJiaolian = entity.getQxJiaolian();
        if (qxJiaolian != null) {
            stmt.bindString(18, qxJiaolian);
        }
 
        String qxLaogu = entity.getQxLaogu();
        if (qxLaogu != null) {
            stmt.bindString(19, qxLaogu);
        }
 
        String qxSuo = entity.getQxSuo();
        if (qxSuo != null) {
            stmt.bindString(20, qxSuo);
        }
 
        String qxZawu = entity.getQxZawu();
        if (qxZawu != null) {
            stmt.bindString(21, qxZawu);
        }
 
        String caizhi = entity.getCaizhi();
        if (caizhi != null) {
            stmt.bindString(22, caizhi);
        }
 
        String chang = entity.getChang();
        if (chang != null) {
            stmt.bindString(23, chang);
        }
 
        String kuan = entity.getKuan();
        if (kuan != null) {
            stmt.bindString(24, kuan);
        }
 
        String zsGao = entity.getZsGao();
        if (zsGao != null) {
            stmt.bindString(25, zsGao);
        }
 
        String zsKuan = entity.getZsKuan();
        if (zsKuan != null) {
            stmt.bindString(26, zsKuan);
        }
 
        String zxGao = entity.getZxGao();
        if (zxGao != null) {
            stmt.bindString(27, zxGao);
        }
 
        String zxKuan = entity.getZxKuan();
        if (zxKuan != null) {
            stmt.bindString(28, zxKuan);
        }
 
        String ysGao = entity.getYsGao();
        if (ysGao != null) {
            stmt.bindString(29, ysGao);
        }
 
        String ysKuan = entity.getYsKuan();
        if (ysKuan != null) {
            stmt.bindString(30, ysKuan);
        }
 
        String yxGao = entity.getYxGao();
        if (yxGao != null) {
            stmt.bindString(31, yxGao);
        }
 
        String yxKuan = entity.getYxKuan();
        if (yxKuan != null) {
            stmt.bindString(32, yxKuan);
        }
 
        List meters = entity.getMeters();
        if (meters != null) {
            stmt.bindString(33, metersConverter.convertToDatabaseValue(meters));
        }
 
        List boxImages = entity.getBoxImages();
        if (boxImages != null) {
            stmt.bindString(34, boxImagesConverter.convertToDatabaseValue(boxImages));
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
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // gps_X
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // gps_Y
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // gps_Z
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // instAddr
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // instLoc
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // describe
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // tmnlAddr
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // tgName
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // boxRows
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // boxCols
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // note
            cursor.getShort(offset + 15) != 0, // isExsit
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // hasQx
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // qxJiaolian
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // qxLaogu
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // qxSuo
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // qxZawu
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // caizhi
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // chang
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // kuan
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // zsGao
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // zsKuan
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // zxGao
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // zxKuan
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // ysGao
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // ysKuan
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // yxGao
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // yxKuan
            cursor.isNull(offset + 32) ? null : metersConverter.convertToEntityProperty(cursor.getString(offset + 32)), // meters
            cursor.isNull(offset + 33) ? null : boxImagesConverter.convertToEntityProperty(cursor.getString(offset + 33)) // boxImages
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MeasBoxBean entity, int offset) {
        entity.setMeasBoxId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBarCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMeasAssetNo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setScanTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGps_X(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGps_Y(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGps_Z(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setInstAddr(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setInstLoc(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDescribe(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setTmnlAddr(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTgName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setBoxRows(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setBoxCols(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setNote(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setIsExsit(cursor.getShort(offset + 15) != 0);
        entity.setHasQx(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setQxJiaolian(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setQxLaogu(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setQxSuo(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setQxZawu(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setCaizhi(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setChang(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setKuan(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setZsGao(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setZsKuan(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setZxGao(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setZxKuan(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setYsGao(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setYsKuan(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setYxGao(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setYxKuan(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setMeters(cursor.isNull(offset + 32) ? null : metersConverter.convertToEntityProperty(cursor.getString(offset + 32)));
        entity.setBoxImages(cursor.isNull(offset + 33) ? null : boxImagesConverter.convertToEntityProperty(cursor.getString(offset + 33)));
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
