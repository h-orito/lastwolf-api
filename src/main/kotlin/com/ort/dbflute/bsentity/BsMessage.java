package com.ort.dbflute.bsentity;

import java.util.List;
import java.util.ArrayList;

import org.dbflute.dbmeta.DBMeta;
import org.dbflute.dbmeta.AbstractEntity;
import org.dbflute.dbmeta.accessory.DomainEntity;
import com.ort.dbflute.allcommon.EntityDefinedCommonColumn;
import com.ort.dbflute.allcommon.DBMetaInstanceHandler;
import com.ort.dbflute.exentity.*;

/**
 * The entity of MESSAGE as TABLE. <br>
 * メッセージ
 * <pre>
 * [primary-key]
 *     MESSAGE_ID, VILLAGE_ID
 *
 * [column]
 *     MESSAGE_ID, VILLAGE_ID, MESSAGE_TYPE_CODE, VILLAGE_DAY_ID, VILLAGE_PLAYER_ID, MESSAGE_CONTENT, MESSAGE_DATETIME, MESSAGE_UNIXTIMESTAMP_MILLI, IS_STRONG, REGISTER_DATETIME, REGISTER_TRACE, UPDATE_DATETIME, UPDATE_TRACE
 *
 * [sequence]
 *     
 *
 * [identity]
 *     MESSAGE_ID
 *
 * [version-no]
 *     
 *
 * [foreign table]
 *     
 *
 * [referrer table]
 *     
 *
 * [foreign property]
 *     
 *
 * [referrer property]
 *     
 *
 * [get/set template]
 * /= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 * Long messageId = entity.getMessageId();
 * Integer villageId = entity.getVillageId();
 * String messageTypeCode = entity.getMessageTypeCode();
 * Integer villageDayId = entity.getVillageDayId();
 * Integer villagePlayerId = entity.getVillagePlayerId();
 * String messageContent = entity.getMessageContent();
 * java.time.LocalDateTime messageDatetime = entity.getMessageDatetime();
 * Long messageUnixtimestampMilli = entity.getMessageUnixtimestampMilli();
 * Boolean isStrong = entity.getIsStrong();
 * java.time.LocalDateTime registerDatetime = entity.getRegisterDatetime();
 * String registerTrace = entity.getRegisterTrace();
 * java.time.LocalDateTime updateDatetime = entity.getUpdateDatetime();
 * String updateTrace = entity.getUpdateTrace();
 * entity.setMessageId(messageId);
 * entity.setVillageId(villageId);
 * entity.setMessageTypeCode(messageTypeCode);
 * entity.setVillageDayId(villageDayId);
 * entity.setVillagePlayerId(villagePlayerId);
 * entity.setMessageContent(messageContent);
 * entity.setMessageDatetime(messageDatetime);
 * entity.setMessageUnixtimestampMilli(messageUnixtimestampMilli);
 * entity.setIsStrong(isStrong);
 * entity.setRegisterDatetime(registerDatetime);
 * entity.setRegisterTrace(registerTrace);
 * entity.setUpdateDatetime(updateDatetime);
 * entity.setUpdateTrace(updateTrace);
 * = = = = = = = = = =/
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsMessage extends AbstractEntity implements DomainEntity, EntityDefinedCommonColumn {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)} */
    protected Long _messageId;

    /** VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)} */
    protected Integer _villageId;

    /** MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)} */
    protected String _messageTypeCode;

    /** VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)} */
    protected Integer _villageDayId;

    /** VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)} */
    protected Integer _villagePlayerId;

    /** MESSAGE_CONTENT: {NotNull, VARCHAR(10000)} */
    protected String _messageContent;

    /** MESSAGE_DATETIME: {NotNull, DATETIME(19)} */
    protected java.time.LocalDateTime _messageDatetime;

    /** MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)} */
    protected Long _messageUnixtimestampMilli;

    /** IS_STRONG: {NotNull, BIT} */
    protected Boolean _isStrong;

    /** REGISTER_DATETIME: {NotNull, DATETIME(19)} */
    protected java.time.LocalDateTime _registerDatetime;

    /** REGISTER_TRACE: {NotNull, VARCHAR(64)} */
    protected String _registerTrace;

    /** UPDATE_DATETIME: {NotNull, DATETIME(19)} */
    protected java.time.LocalDateTime _updateDatetime;

    /** UPDATE_TRACE: {NotNull, VARCHAR(64)} */
    protected String _updateTrace;

    // ===================================================================================
    //                                                                             DB Meta
    //                                                                             =======
    /** {@inheritDoc} */
    public DBMeta asDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(asTableDbName());
    }

    /** {@inheritDoc} */
    public String asTableDbName() {
        return "message";
    }

    // ===================================================================================
    //                                                                        Key Handling
    //                                                                        ============
    /** {@inheritDoc} */
    public boolean hasPrimaryKeyValue() {
        if (_messageId == null) { return false; }
        if (_villageId == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    protected <ELEMENT> List<ELEMENT> newReferrerList() { // overriding to import
        return new ArrayList<ELEMENT>();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    protected boolean doEquals(Object obj) {
        if (obj instanceof BsMessage) {
            BsMessage other = (BsMessage)obj;
            if (!xSV(_messageId, other._messageId)) { return false; }
            if (!xSV(_villageId, other._villageId)) { return false; }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int doHashCode(int initial) {
        int hs = initial;
        hs = xCH(hs, asTableDbName());
        hs = xCH(hs, _messageId);
        hs = xCH(hs, _villageId);
        return hs;
    }

    @Override
    protected String doBuildStringWithRelation(String li) {
        return "";
    }

    @Override
    protected String doBuildColumnString(String dm) {
        StringBuilder sb = new StringBuilder();
        sb.append(dm).append(xfND(_messageId));
        sb.append(dm).append(xfND(_villageId));
        sb.append(dm).append(xfND(_messageTypeCode));
        sb.append(dm).append(xfND(_villageDayId));
        sb.append(dm).append(xfND(_villagePlayerId));
        sb.append(dm).append(xfND(_messageContent));
        sb.append(dm).append(xfND(_messageDatetime));
        sb.append(dm).append(xfND(_messageUnixtimestampMilli));
        sb.append(dm).append(xfND(_isStrong));
        sb.append(dm).append(xfND(_registerDatetime));
        sb.append(dm).append(xfND(_registerTrace));
        sb.append(dm).append(xfND(_updateDatetime));
        sb.append(dm).append(xfND(_updateTrace));
        if (sb.length() > dm.length()) {
            sb.delete(0, dm.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }

    @Override
    protected String doBuildRelationString(String dm) {
        return "";
    }

    @Override
    public Message clone() {
        return (Message)super.clone();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)} <br>
     * メッセージID
     * @return The value of the column 'MESSAGE_ID'. (basically NotNull if selected: for the constraint)
     */
    public Long getMessageId() {
        checkSpecifiedProperty("messageId");
        return _messageId;
    }

    /**
     * [set] MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)} <br>
     * メッセージID
     * @param messageId The value of the column 'MESSAGE_ID'. (basically NotNull if update: for the constraint)
     */
    public void setMessageId(Long messageId) {
        registerModifiedProperty("messageId");
        _messageId = messageId;
    }

    /**
     * [get] VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)} <br>
     * 村ID
     * @return The value of the column 'VILLAGE_ID'. (basically NotNull if selected: for the constraint)
     */
    public Integer getVillageId() {
        checkSpecifiedProperty("villageId");
        return _villageId;
    }

    /**
     * [set] VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)} <br>
     * 村ID
     * @param villageId The value of the column 'VILLAGE_ID'. (basically NotNull if update: for the constraint)
     */
    public void setVillageId(Integer villageId) {
        registerModifiedProperty("villageId");
        _villageId = villageId;
    }

    /**
     * [get] MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)} <br>
     * メッセージ種別コード
     * @return The value of the column 'MESSAGE_TYPE_CODE'. (basically NotNull if selected: for the constraint)
     */
    public String getMessageTypeCode() {
        checkSpecifiedProperty("messageTypeCode");
        return convertEmptyToNull(_messageTypeCode);
    }

    /**
     * [set] MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)} <br>
     * メッセージ種別コード
     * @param messageTypeCode The value of the column 'MESSAGE_TYPE_CODE'. (basically NotNull if update: for the constraint)
     */
    public void setMessageTypeCode(String messageTypeCode) {
        registerModifiedProperty("messageTypeCode");
        _messageTypeCode = messageTypeCode;
    }

    /**
     * [get] VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)} <br>
     * 村日付ID
     * @return The value of the column 'VILLAGE_DAY_ID'. (basically NotNull if selected: for the constraint)
     */
    public Integer getVillageDayId() {
        checkSpecifiedProperty("villageDayId");
        return _villageDayId;
    }

    /**
     * [set] VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)} <br>
     * 村日付ID
     * @param villageDayId The value of the column 'VILLAGE_DAY_ID'. (basically NotNull if update: for the constraint)
     */
    public void setVillageDayId(Integer villageDayId) {
        registerModifiedProperty("villageDayId");
        _villageDayId = villageDayId;
    }

    /**
     * [get] VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)} <br>
     * 村参加者ID
     * @return The value of the column 'VILLAGE_PLAYER_ID'. (NullAllowed even if selected: for no constraint)
     */
    public Integer getVillagePlayerId() {
        checkSpecifiedProperty("villagePlayerId");
        return _villagePlayerId;
    }

    /**
     * [set] VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)} <br>
     * 村参加者ID
     * @param villagePlayerId The value of the column 'VILLAGE_PLAYER_ID'. (NullAllowed: null update allowed for no constraint)
     */
    public void setVillagePlayerId(Integer villagePlayerId) {
        registerModifiedProperty("villagePlayerId");
        _villagePlayerId = villagePlayerId;
    }

    /**
     * [get] MESSAGE_CONTENT: {NotNull, VARCHAR(10000)} <br>
     * メッセージ内容
     * @return The value of the column 'MESSAGE_CONTENT'. (basically NotNull if selected: for the constraint)
     */
    public String getMessageContent() {
        checkSpecifiedProperty("messageContent");
        return convertEmptyToNull(_messageContent);
    }

    /**
     * [set] MESSAGE_CONTENT: {NotNull, VARCHAR(10000)} <br>
     * メッセージ内容
     * @param messageContent The value of the column 'MESSAGE_CONTENT'. (basically NotNull if update: for the constraint)
     */
    public void setMessageContent(String messageContent) {
        registerModifiedProperty("messageContent");
        _messageContent = messageContent;
    }

    /**
     * [get] MESSAGE_DATETIME: {NotNull, DATETIME(19)} <br>
     * メッセージ日時
     * @return The value of the column 'MESSAGE_DATETIME'. (basically NotNull if selected: for the constraint)
     */
    public java.time.LocalDateTime getMessageDatetime() {
        checkSpecifiedProperty("messageDatetime");
        return _messageDatetime;
    }

    /**
     * [set] MESSAGE_DATETIME: {NotNull, DATETIME(19)} <br>
     * メッセージ日時
     * @param messageDatetime The value of the column 'MESSAGE_DATETIME'. (basically NotNull if update: for the constraint)
     */
    public void setMessageDatetime(java.time.LocalDateTime messageDatetime) {
        registerModifiedProperty("messageDatetime");
        _messageDatetime = messageDatetime;
    }

    /**
     * [get] MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)} <br>
     * メッセージUNIXタイムスタンプミリ秒
     * @return The value of the column 'MESSAGE_UNIXTIMESTAMP_MILLI'. (basically NotNull if selected: for the constraint)
     */
    public Long getMessageUnixtimestampMilli() {
        checkSpecifiedProperty("messageUnixtimestampMilli");
        return _messageUnixtimestampMilli;
    }

    /**
     * [set] MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)} <br>
     * メッセージUNIXタイムスタンプミリ秒
     * @param messageUnixtimestampMilli The value of the column 'MESSAGE_UNIXTIMESTAMP_MILLI'. (basically NotNull if update: for the constraint)
     */
    public void setMessageUnixtimestampMilli(Long messageUnixtimestampMilli) {
        registerModifiedProperty("messageUnixtimestampMilli");
        _messageUnixtimestampMilli = messageUnixtimestampMilli;
    }

    /**
     * [get] IS_STRONG: {NotNull, BIT} <br>
     * 強調発言か
     * @return The value of the column 'IS_STRONG'. (basically NotNull if selected: for the constraint)
     */
    public Boolean getIsStrong() {
        checkSpecifiedProperty("isStrong");
        return _isStrong;
    }

    /**
     * [set] IS_STRONG: {NotNull, BIT} <br>
     * 強調発言か
     * @param isStrong The value of the column 'IS_STRONG'. (basically NotNull if update: for the constraint)
     */
    public void setIsStrong(Boolean isStrong) {
        registerModifiedProperty("isStrong");
        _isStrong = isStrong;
    }

    /**
     * [get] REGISTER_DATETIME: {NotNull, DATETIME(19)} <br>
     * 登録日時
     * @return The value of the column 'REGISTER_DATETIME'. (basically NotNull if selected: for the constraint)
     */
    public java.time.LocalDateTime getRegisterDatetime() {
        checkSpecifiedProperty("registerDatetime");
        return _registerDatetime;
    }

    /**
     * [set] REGISTER_DATETIME: {NotNull, DATETIME(19)} <br>
     * 登録日時
     * @param registerDatetime The value of the column 'REGISTER_DATETIME'. (basically NotNull if update: for the constraint)
     */
    public void setRegisterDatetime(java.time.LocalDateTime registerDatetime) {
        registerModifiedProperty("registerDatetime");
        _registerDatetime = registerDatetime;
    }

    /**
     * [get] REGISTER_TRACE: {NotNull, VARCHAR(64)} <br>
     * 登録トレース
     * @return The value of the column 'REGISTER_TRACE'. (basically NotNull if selected: for the constraint)
     */
    public String getRegisterTrace() {
        checkSpecifiedProperty("registerTrace");
        return convertEmptyToNull(_registerTrace);
    }

    /**
     * [set] REGISTER_TRACE: {NotNull, VARCHAR(64)} <br>
     * 登録トレース
     * @param registerTrace The value of the column 'REGISTER_TRACE'. (basically NotNull if update: for the constraint)
     */
    public void setRegisterTrace(String registerTrace) {
        registerModifiedProperty("registerTrace");
        _registerTrace = registerTrace;
    }

    /**
     * [get] UPDATE_DATETIME: {NotNull, DATETIME(19)} <br>
     * 更新日時
     * @return The value of the column 'UPDATE_DATETIME'. (basically NotNull if selected: for the constraint)
     */
    public java.time.LocalDateTime getUpdateDatetime() {
        checkSpecifiedProperty("updateDatetime");
        return _updateDatetime;
    }

    /**
     * [set] UPDATE_DATETIME: {NotNull, DATETIME(19)} <br>
     * 更新日時
     * @param updateDatetime The value of the column 'UPDATE_DATETIME'. (basically NotNull if update: for the constraint)
     */
    public void setUpdateDatetime(java.time.LocalDateTime updateDatetime) {
        registerModifiedProperty("updateDatetime");
        _updateDatetime = updateDatetime;
    }

    /**
     * [get] UPDATE_TRACE: {NotNull, VARCHAR(64)} <br>
     * 更新トレース
     * @return The value of the column 'UPDATE_TRACE'. (basically NotNull if selected: for the constraint)
     */
    public String getUpdateTrace() {
        checkSpecifiedProperty("updateTrace");
        return convertEmptyToNull(_updateTrace);
    }

    /**
     * [set] UPDATE_TRACE: {NotNull, VARCHAR(64)} <br>
     * 更新トレース
     * @param updateTrace The value of the column 'UPDATE_TRACE'. (basically NotNull if update: for the constraint)
     */
    public void setUpdateTrace(String updateTrace) {
        registerModifiedProperty("updateTrace");
        _updateTrace = updateTrace;
    }
}
