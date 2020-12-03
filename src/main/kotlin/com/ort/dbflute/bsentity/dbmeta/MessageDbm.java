package com.ort.dbflute.bsentity.dbmeta;

import java.util.List;
import java.util.Map;

import org.dbflute.Entity;
import org.dbflute.dbmeta.AbstractDBMeta;
import org.dbflute.dbmeta.info.*;
import org.dbflute.dbmeta.name.*;
import org.dbflute.dbmeta.property.PropertyGateway;
import org.dbflute.dbway.DBDef;
import com.ort.dbflute.allcommon.*;
import com.ort.dbflute.exentity.*;

/**
 * The DB meta of message. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class MessageDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final MessageDbm _instance = new MessageDbm();
    private MessageDbm() {}
    public static MessageDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public String getProjectName() { return DBCurrent.getInstance().projectName(); }
    public String getProjectPrefix() { return DBCurrent.getInstance().projectPrefix(); }
    public String getGenerationGapBasePrefix() { return DBCurrent.getInstance().generationGapBasePrefix(); }
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                    Property Gateway
    //                                                                    ================
    // -----------------------------------------------------
    //                                       Column Property
    //                                       ---------------
    protected final Map<String, PropertyGateway> _epgMap = newHashMap();
    { xsetupEpg(); }
    protected void xsetupEpg() {
        setupEpg(_epgMap, et -> ((Message)et).getMessageId(), (et, vl) -> ((Message)et).setMessageId(ctl(vl)), "messageId");
        setupEpg(_epgMap, et -> ((Message)et).getVillageId(), (et, vl) -> ((Message)et).setVillageId(cti(vl)), "villageId");
        setupEpg(_epgMap, et -> ((Message)et).getMessageTypeCode(), (et, vl) -> ((Message)et).setMessageTypeCode((String)vl), "messageTypeCode");
        setupEpg(_epgMap, et -> ((Message)et).getVillageDayId(), (et, vl) -> ((Message)et).setVillageDayId(cti(vl)), "villageDayId");
        setupEpg(_epgMap, et -> ((Message)et).getVillagePlayerId(), (et, vl) -> ((Message)et).setVillagePlayerId(cti(vl)), "villagePlayerId");
        setupEpg(_epgMap, et -> ((Message)et).getMessageContent(), (et, vl) -> ((Message)et).setMessageContent((String)vl), "messageContent");
        setupEpg(_epgMap, et -> ((Message)et).getMessageDatetime(), (et, vl) -> ((Message)et).setMessageDatetime(ctldt(vl)), "messageDatetime");
        setupEpg(_epgMap, et -> ((Message)et).getMessageUnixtimestampMilli(), (et, vl) -> ((Message)et).setMessageUnixtimestampMilli(ctl(vl)), "messageUnixtimestampMilli");
        setupEpg(_epgMap, et -> ((Message)et).getIsStrong(), (et, vl) -> ((Message)et).setIsStrong((Boolean)vl), "isStrong");
        setupEpg(_epgMap, et -> ((Message)et).getRegisterDatetime(), (et, vl) -> ((Message)et).setRegisterDatetime(ctldt(vl)), "registerDatetime");
        setupEpg(_epgMap, et -> ((Message)et).getRegisterTrace(), (et, vl) -> ((Message)et).setRegisterTrace((String)vl), "registerTrace");
        setupEpg(_epgMap, et -> ((Message)et).getUpdateDatetime(), (et, vl) -> ((Message)et).setUpdateDatetime(ctldt(vl)), "updateDatetime");
        setupEpg(_epgMap, et -> ((Message)et).getUpdateTrace(), (et, vl) -> ((Message)et).setUpdateTrace((String)vl), "updateTrace");
    }
    public PropertyGateway findPropertyGateway(String prop)
    { return doFindEpg(_epgMap, prop); }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "message";
    protected final String _tableDispName = "MESSAGE";
    protected final String _tablePropertyName = "message";
    protected final TableSqlName _tableSqlName = new TableSqlName("MESSAGE", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTableDispName() { return _tableDispName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnMessageId = cci("MESSAGE_ID", "MESSAGE_ID", null, null, Long.class, "messageId", null, true, true, true, "BIGINT", 19, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnVillageId = cci("VILLAGE_ID", "VILLAGE_ID", null, null, Integer.class, "villageId", null, true, false, true, "INT UNSIGNED", 10, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnMessageTypeCode = cci("MESSAGE_TYPE_CODE", "MESSAGE_TYPE_CODE", null, null, String.class, "messageTypeCode", null, false, false, true, "VARCHAR", 20, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnVillageDayId = cci("VILLAGE_DAY_ID", "VILLAGE_DAY_ID", null, null, Integer.class, "villageDayId", null, false, false, true, "INT UNSIGNED", 10, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnVillagePlayerId = cci("VILLAGE_PLAYER_ID", "VILLAGE_PLAYER_ID", null, null, Integer.class, "villagePlayerId", null, false, false, false, "INT UNSIGNED", 10, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnMessageContent = cci("MESSAGE_CONTENT", "MESSAGE_CONTENT", null, null, String.class, "messageContent", null, false, false, true, "VARCHAR", 10000, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnMessageDatetime = cci("MESSAGE_DATETIME", "MESSAGE_DATETIME", null, null, java.time.LocalDateTime.class, "messageDatetime", null, false, false, true, "DATETIME", 19, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnMessageUnixtimestampMilli = cci("MESSAGE_UNIXTIMESTAMP_MILLI", "MESSAGE_UNIXTIMESTAMP_MILLI", null, null, Long.class, "messageUnixtimestampMilli", null, false, false, true, "BIGINT UNSIGNED", 20, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnIsStrong = cci("IS_STRONG", "IS_STRONG", null, null, Boolean.class, "isStrong", null, false, false, true, "BIT", null, null, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnRegisterDatetime = cci("REGISTER_DATETIME", "REGISTER_DATETIME", null, null, java.time.LocalDateTime.class, "registerDatetime", null, false, false, true, "DATETIME", 19, 0, null, null, true, null, null, null, null, null, false);
    protected final ColumnInfo _columnRegisterTrace = cci("REGISTER_TRACE", "REGISTER_TRACE", null, null, String.class, "registerTrace", null, false, false, true, "VARCHAR", 64, 0, null, null, true, null, null, null, null, null, false);
    protected final ColumnInfo _columnUpdateDatetime = cci("UPDATE_DATETIME", "UPDATE_DATETIME", null, null, java.time.LocalDateTime.class, "updateDatetime", null, false, false, true, "DATETIME", 19, 0, null, null, true, null, null, null, null, null, false);
    protected final ColumnInfo _columnUpdateTrace = cci("UPDATE_TRACE", "UPDATE_TRACE", null, null, String.class, "updateTrace", null, false, false, true, "VARCHAR", 64, 0, null, null, true, null, null, null, null, null, false);

    /**
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnMessageId() { return _columnMessageId; }
    /**
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnVillageId() { return _columnVillageId; }
    /**
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnMessageTypeCode() { return _columnMessageTypeCode; }
    /**
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnVillageDayId() { return _columnVillageDayId; }
    /**
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnVillagePlayerId() { return _columnVillagePlayerId; }
    /**
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnMessageContent() { return _columnMessageContent; }
    /**
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnMessageDatetime() { return _columnMessageDatetime; }
    /**
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnMessageUnixtimestampMilli() { return _columnMessageUnixtimestampMilli; }
    /**
     * IS_STRONG: {NotNull, BIT}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnIsStrong() { return _columnIsStrong; }
    /**
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnRegisterDatetime() { return _columnRegisterDatetime; }
    /**
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnRegisterTrace() { return _columnRegisterTrace; }
    /**
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnUpdateDatetime() { return _columnUpdateDatetime; }
    /**
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnUpdateTrace() { return _columnUpdateTrace; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnMessageId());
        ls.add(columnVillageId());
        ls.add(columnMessageTypeCode());
        ls.add(columnVillageDayId());
        ls.add(columnVillagePlayerId());
        ls.add(columnMessageContent());
        ls.add(columnMessageDatetime());
        ls.add(columnMessageUnixtimestampMilli());
        ls.add(columnIsStrong());
        ls.add(columnRegisterDatetime());
        ls.add(columnRegisterTrace());
        ls.add(columnUpdateDatetime());
        ls.add(columnUpdateTrace());
        return ls;
    }

    { initializeInformationResource(); }

    // ===================================================================================
    //                                                                         Unique Info
    //                                                                         ===========
    // -----------------------------------------------------
    //                                       Primary Element
    //                                       ---------------
    protected UniqueInfo cpui() {
        List<ColumnInfo> ls = newArrayListSized(4);
        ls.add(columnMessageId());
        ls.add(columnVillageId());
        return hpcpui(ls);
    }
    public boolean hasPrimaryKey() { return true; }
    public boolean hasCompoundPrimaryKey() { return true; }

    // ===================================================================================
    //                                                                       Relation Info
    //                                                                       =============
    // cannot cache because it uses related DB meta instance while booting
    // (instead, cached by super's collection)
    // -----------------------------------------------------
    //                                      Foreign Property
    //                                      ----------------

    // -----------------------------------------------------
    //                                     Referrer Property
    //                                     -----------------

    // ===================================================================================
    //                                                                        Various Info
    //                                                                        ============
    public boolean hasIdentity() { return true; }
    public boolean hasCommonColumn() { return true; }
    public List<ColumnInfo> getCommonColumnInfoList()
    { return newArrayList(columnRegisterDatetime(), columnRegisterTrace(), columnUpdateDatetime(), columnUpdateTrace()); }
    public List<ColumnInfo> getCommonColumnInfoBeforeInsertList()
    { return newArrayList(columnRegisterDatetime(), columnRegisterTrace(), columnUpdateDatetime(), columnUpdateTrace()); }
    public List<ColumnInfo> getCommonColumnInfoBeforeUpdateList()
    { return newArrayList(columnUpdateDatetime(), columnUpdateTrace()); }

    // ===================================================================================
    //                                                                           Type Name
    //                                                                           =========
    public String getEntityTypeName() { return "com.ort.dbflute.exentity.Message"; }
    public String getConditionBeanTypeName() { return "com.ort.dbflute.cbean.MessageCB"; }
    public String getBehaviorTypeName() { return "com.ort.dbflute.exbhv.MessageBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<Message> getEntityType() { return Message.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public Message newEntity() { return new Message(); }

    // ===================================================================================
    //                                                                   Map Communication
    //                                                                   =================
    public void acceptPrimaryKeyMap(Entity et, Map<String, ? extends Object> mp)
    { doAcceptPrimaryKeyMap((Message)et, mp); }
    public void acceptAllColumnMap(Entity et, Map<String, ? extends Object> mp)
    { doAcceptAllColumnMap((Message)et, mp); }
    public Map<String, Object> extractPrimaryKeyMap(Entity et) { return doExtractPrimaryKeyMap(et); }
    public Map<String, Object> extractAllColumnMap(Entity et) { return doExtractAllColumnMap(et); }
}
