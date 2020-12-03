package com.ort.dbflute.cbean.cq.bs;

import java.util.*;

import org.dbflute.cbean.*;
import org.dbflute.cbean.chelper.*;
import org.dbflute.cbean.ckey.*;
import org.dbflute.cbean.coption.*;
import org.dbflute.cbean.cvalue.ConditionValue;
import org.dbflute.cbean.ordering.*;
import org.dbflute.cbean.scoping.*;
import org.dbflute.cbean.sqlclause.SqlClause;
import org.dbflute.dbmeta.DBMetaProvider;
import com.ort.dbflute.allcommon.*;
import com.ort.dbflute.cbean.*;
import com.ort.dbflute.cbean.cq.*;

/**
 * The abstract condition-query of message.
 * @author DBFlute(AutoGenerator)
 */
public abstract class AbstractBsMessageCQ extends AbstractConditionQuery {

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsMessageCQ(ConditionQuery referrerQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(referrerQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                             DB Meta
    //                                                                             =======
    @Override
    protected DBMetaProvider xgetDBMetaProvider() {
        return DBMetaInstanceHandler.getProvider();
    }

    public String asTableDbName() {
        return "message";
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageId The value of messageId as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageId_Equal(Long messageId) {
        doSetMessageId_Equal(messageId);
    }

    protected void doSetMessageId_Equal(Long messageId) {
        regMessageId(CK_EQ, messageId);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageId The value of messageId as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageId_NotEqual(Long messageId) {
        doSetMessageId_NotEqual(messageId);
    }

    protected void doSetMessageId_NotEqual(Long messageId) {
        regMessageId(CK_NES, messageId);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageId The value of messageId as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageId_GreaterThan(Long messageId) {
        regMessageId(CK_GT, messageId);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageId The value of messageId as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageId_LessThan(Long messageId) {
        regMessageId(CK_LT, messageId);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageId The value of messageId as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageId_GreaterEqual(Long messageId) {
        regMessageId(CK_GE, messageId);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageId The value of messageId as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageId_LessEqual(Long messageId) {
        regMessageId(CK_LE, messageId);
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param minNumber The min number of messageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of messageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of range-of. (NotNull)
     */
    public void setMessageId_RangeOf(Long minNumber, Long maxNumber, ConditionOptionCall<RangeOfOption> opLambda) {
        setMessageId_RangeOf(minNumber, maxNumber, xcROOP(opLambda));
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param minNumber The min number of messageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of messageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param rangeOfOption The option of range-of. (NotNull)
     */
    protected void setMessageId_RangeOf(Long minNumber, Long maxNumber, RangeOfOption rangeOfOption) {
        regROO(minNumber, maxNumber, xgetCValueMessageId(), "MESSAGE_ID", rangeOfOption);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageIdList The collection of messageId as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageId_InScope(Collection<Long> messageIdList) {
        doSetMessageId_InScope(messageIdList);
    }

    protected void doSetMessageId_InScope(Collection<Long> messageIdList) {
        regINS(CK_INS, cTL(messageIdList), xgetCValueMessageId(), "MESSAGE_ID");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     * @param messageIdList The collection of messageId as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageId_NotInScope(Collection<Long> messageIdList) {
        doSetMessageId_NotInScope(messageIdList);
    }

    protected void doSetMessageId_NotInScope(Collection<Long> messageIdList) {
        regINS(CK_NINS, cTL(messageIdList), xgetCValueMessageId(), "MESSAGE_ID");
    }

    /**
     * IsNull {is null}. And OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     */
    public void setMessageId_IsNull() { regMessageId(CK_ISN, DOBJ); }

    /**
     * IsNotNull {is not null}. And OnlyOnceRegistered. <br>
     * MESSAGE_ID: {PK, ID, NotNull, BIGINT(19)}
     */
    public void setMessageId_IsNotNull() { regMessageId(CK_ISNN, DOBJ); }

    protected void regMessageId(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueMessageId(), "MESSAGE_ID"); }
    protected abstract ConditionValue xgetCValueMessageId();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageId The value of villageId as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageId_Equal(Integer villageId) {
        doSetVillageId_Equal(villageId);
    }

    protected void doSetVillageId_Equal(Integer villageId) {
        regVillageId(CK_EQ, villageId);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageId The value of villageId as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageId_NotEqual(Integer villageId) {
        doSetVillageId_NotEqual(villageId);
    }

    protected void doSetVillageId_NotEqual(Integer villageId) {
        regVillageId(CK_NES, villageId);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageId The value of villageId as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageId_GreaterThan(Integer villageId) {
        regVillageId(CK_GT, villageId);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageId The value of villageId as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageId_LessThan(Integer villageId) {
        regVillageId(CK_LT, villageId);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageId The value of villageId as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageId_GreaterEqual(Integer villageId) {
        regVillageId(CK_GE, villageId);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageId The value of villageId as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageId_LessEqual(Integer villageId) {
        regVillageId(CK_LE, villageId);
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param minNumber The min number of villageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of villageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of range-of. (NotNull)
     */
    public void setVillageId_RangeOf(Integer minNumber, Integer maxNumber, ConditionOptionCall<RangeOfOption> opLambda) {
        setVillageId_RangeOf(minNumber, maxNumber, xcROOP(opLambda));
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param minNumber The min number of villageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of villageId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param rangeOfOption The option of range-of. (NotNull)
     */
    protected void setVillageId_RangeOf(Integer minNumber, Integer maxNumber, RangeOfOption rangeOfOption) {
        regROO(minNumber, maxNumber, xgetCValueVillageId(), "VILLAGE_ID", rangeOfOption);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageIdList The collection of villageId as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setVillageId_InScope(Collection<Integer> villageIdList) {
        doSetVillageId_InScope(villageIdList);
    }

    protected void doSetVillageId_InScope(Collection<Integer> villageIdList) {
        regINS(CK_INS, cTL(villageIdList), xgetCValueVillageId(), "VILLAGE_ID");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     * @param villageIdList The collection of villageId as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setVillageId_NotInScope(Collection<Integer> villageIdList) {
        doSetVillageId_NotInScope(villageIdList);
    }

    protected void doSetVillageId_NotInScope(Collection<Integer> villageIdList) {
        regINS(CK_NINS, cTL(villageIdList), xgetCValueVillageId(), "VILLAGE_ID");
    }

    /**
     * IsNull {is null}. And OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     */
    public void setVillageId_IsNull() { regVillageId(CK_ISN, DOBJ); }

    /**
     * IsNotNull {is not null}. And OnlyOnceRegistered. <br>
     * VILLAGE_ID: {PK, IX, NotNull, INT UNSIGNED(10)}
     */
    public void setVillageId_IsNotNull() { regVillageId(CK_ISNN, DOBJ); }

    protected void regVillageId(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueVillageId(), "VILLAGE_ID"); }
    protected abstract ConditionValue xgetCValueVillageId();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_Equal(String messageTypeCode) {
        doSetMessageTypeCode_Equal(fRES(messageTypeCode));
    }

    protected void doSetMessageTypeCode_Equal(String messageTypeCode) {
        regMessageTypeCode(CK_EQ, messageTypeCode);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_NotEqual(String messageTypeCode) {
        doSetMessageTypeCode_NotEqual(fRES(messageTypeCode));
    }

    protected void doSetMessageTypeCode_NotEqual(String messageTypeCode) {
        regMessageTypeCode(CK_NES, messageTypeCode);
    }

    /**
     * GreaterThan(&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as greaterThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_GreaterThan(String messageTypeCode) {
        regMessageTypeCode(CK_GT, fRES(messageTypeCode));
    }

    /**
     * LessThan(&lt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as lessThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_LessThan(String messageTypeCode) {
        regMessageTypeCode(CK_LT, fRES(messageTypeCode));
    }

    /**
     * GreaterEqual(&gt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as greaterEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_GreaterEqual(String messageTypeCode) {
        regMessageTypeCode(CK_GE, fRES(messageTypeCode));
    }

    /**
     * LessEqual(&lt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as lessEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_LessEqual(String messageTypeCode) {
        regMessageTypeCode(CK_LE, fRES(messageTypeCode));
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCodeList The collection of messageTypeCode as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_InScope(Collection<String> messageTypeCodeList) {
        doSetMessageTypeCode_InScope(messageTypeCodeList);
    }

    protected void doSetMessageTypeCode_InScope(Collection<String> messageTypeCodeList) {
        regINS(CK_INS, cTL(messageTypeCodeList), xgetCValueMessageTypeCode(), "MESSAGE_TYPE_CODE");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCodeList The collection of messageTypeCode as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageTypeCode_NotInScope(Collection<String> messageTypeCodeList) {
        doSetMessageTypeCode_NotInScope(messageTypeCodeList);
    }

    protected void doSetMessageTypeCode_NotInScope(Collection<String> messageTypeCodeList) {
        regINS(CK_NINS, cTL(messageTypeCodeList), xgetCValueMessageTypeCode(), "MESSAGE_TYPE_CODE");
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)} <br>
     * <pre>e.g. setMessageTypeCode_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param messageTypeCode The value of messageTypeCode as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setMessageTypeCode_LikeSearch(String messageTypeCode, ConditionOptionCall<LikeSearchOption> opLambda) {
        setMessageTypeCode_LikeSearch(messageTypeCode, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)} <br>
     * <pre>e.g. setMessageTypeCode_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param messageTypeCode The value of messageTypeCode as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setMessageTypeCode_LikeSearch(String messageTypeCode, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(messageTypeCode), xgetCValueMessageTypeCode(), "MESSAGE_TYPE_CODE", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setMessageTypeCode_NotLikeSearch(String messageTypeCode, ConditionOptionCall<LikeSearchOption> opLambda) {
        setMessageTypeCode_NotLikeSearch(messageTypeCode, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_TYPE_CODE: {IX, NotNull, VARCHAR(20)}
     * @param messageTypeCode The value of messageTypeCode as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setMessageTypeCode_NotLikeSearch(String messageTypeCode, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(messageTypeCode), xgetCValueMessageTypeCode(), "MESSAGE_TYPE_CODE", likeSearchOption);
    }

    protected void regMessageTypeCode(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueMessageTypeCode(), "MESSAGE_TYPE_CODE"); }
    protected abstract ConditionValue xgetCValueMessageTypeCode();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayId The value of villageDayId as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageDayId_Equal(Integer villageDayId) {
        doSetVillageDayId_Equal(villageDayId);
    }

    protected void doSetVillageDayId_Equal(Integer villageDayId) {
        regVillageDayId(CK_EQ, villageDayId);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayId The value of villageDayId as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageDayId_NotEqual(Integer villageDayId) {
        doSetVillageDayId_NotEqual(villageDayId);
    }

    protected void doSetVillageDayId_NotEqual(Integer villageDayId) {
        regVillageDayId(CK_NES, villageDayId);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayId The value of villageDayId as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageDayId_GreaterThan(Integer villageDayId) {
        regVillageDayId(CK_GT, villageDayId);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayId The value of villageDayId as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageDayId_LessThan(Integer villageDayId) {
        regVillageDayId(CK_LT, villageDayId);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayId The value of villageDayId as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageDayId_GreaterEqual(Integer villageDayId) {
        regVillageDayId(CK_GE, villageDayId);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayId The value of villageDayId as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillageDayId_LessEqual(Integer villageDayId) {
        regVillageDayId(CK_LE, villageDayId);
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param minNumber The min number of villageDayId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of villageDayId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of range-of. (NotNull)
     */
    public void setVillageDayId_RangeOf(Integer minNumber, Integer maxNumber, ConditionOptionCall<RangeOfOption> opLambda) {
        setVillageDayId_RangeOf(minNumber, maxNumber, xcROOP(opLambda));
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param minNumber The min number of villageDayId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of villageDayId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param rangeOfOption The option of range-of. (NotNull)
     */
    protected void setVillageDayId_RangeOf(Integer minNumber, Integer maxNumber, RangeOfOption rangeOfOption) {
        regROO(minNumber, maxNumber, xgetCValueVillageDayId(), "VILLAGE_DAY_ID", rangeOfOption);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayIdList The collection of villageDayId as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setVillageDayId_InScope(Collection<Integer> villageDayIdList) {
        doSetVillageDayId_InScope(villageDayIdList);
    }

    protected void doSetVillageDayId_InScope(Collection<Integer> villageDayIdList) {
        regINS(CK_INS, cTL(villageDayIdList), xgetCValueVillageDayId(), "VILLAGE_DAY_ID");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * VILLAGE_DAY_ID: {IX, NotNull, INT UNSIGNED(10)}
     * @param villageDayIdList The collection of villageDayId as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setVillageDayId_NotInScope(Collection<Integer> villageDayIdList) {
        doSetVillageDayId_NotInScope(villageDayIdList);
    }

    protected void doSetVillageDayId_NotInScope(Collection<Integer> villageDayIdList) {
        regINS(CK_NINS, cTL(villageDayIdList), xgetCValueVillageDayId(), "VILLAGE_DAY_ID");
    }

    protected void regVillageDayId(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueVillageDayId(), "VILLAGE_DAY_ID"); }
    protected abstract ConditionValue xgetCValueVillageDayId();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerId The value of villagePlayerId as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillagePlayerId_Equal(Integer villagePlayerId) {
        doSetVillagePlayerId_Equal(villagePlayerId);
    }

    protected void doSetVillagePlayerId_Equal(Integer villagePlayerId) {
        regVillagePlayerId(CK_EQ, villagePlayerId);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerId The value of villagePlayerId as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillagePlayerId_NotEqual(Integer villagePlayerId) {
        doSetVillagePlayerId_NotEqual(villagePlayerId);
    }

    protected void doSetVillagePlayerId_NotEqual(Integer villagePlayerId) {
        regVillagePlayerId(CK_NES, villagePlayerId);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerId The value of villagePlayerId as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillagePlayerId_GreaterThan(Integer villagePlayerId) {
        regVillagePlayerId(CK_GT, villagePlayerId);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerId The value of villagePlayerId as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillagePlayerId_LessThan(Integer villagePlayerId) {
        regVillagePlayerId(CK_LT, villagePlayerId);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerId The value of villagePlayerId as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillagePlayerId_GreaterEqual(Integer villagePlayerId) {
        regVillagePlayerId(CK_GE, villagePlayerId);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerId The value of villagePlayerId as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setVillagePlayerId_LessEqual(Integer villagePlayerId) {
        regVillagePlayerId(CK_LE, villagePlayerId);
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param minNumber The min number of villagePlayerId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of villagePlayerId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of range-of. (NotNull)
     */
    public void setVillagePlayerId_RangeOf(Integer minNumber, Integer maxNumber, ConditionOptionCall<RangeOfOption> opLambda) {
        setVillagePlayerId_RangeOf(minNumber, maxNumber, xcROOP(opLambda));
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param minNumber The min number of villagePlayerId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of villagePlayerId. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param rangeOfOption The option of range-of. (NotNull)
     */
    protected void setVillagePlayerId_RangeOf(Integer minNumber, Integer maxNumber, RangeOfOption rangeOfOption) {
        regROO(minNumber, maxNumber, xgetCValueVillagePlayerId(), "VILLAGE_PLAYER_ID", rangeOfOption);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerIdList The collection of villagePlayerId as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setVillagePlayerId_InScope(Collection<Integer> villagePlayerIdList) {
        doSetVillagePlayerId_InScope(villagePlayerIdList);
    }

    protected void doSetVillagePlayerId_InScope(Collection<Integer> villagePlayerIdList) {
        regINS(CK_INS, cTL(villagePlayerIdList), xgetCValueVillagePlayerId(), "VILLAGE_PLAYER_ID");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     * @param villagePlayerIdList The collection of villagePlayerId as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setVillagePlayerId_NotInScope(Collection<Integer> villagePlayerIdList) {
        doSetVillagePlayerId_NotInScope(villagePlayerIdList);
    }

    protected void doSetVillagePlayerId_NotInScope(Collection<Integer> villagePlayerIdList) {
        regINS(CK_NINS, cTL(villagePlayerIdList), xgetCValueVillagePlayerId(), "VILLAGE_PLAYER_ID");
    }

    /**
     * IsNull {is null}. And OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     */
    public void setVillagePlayerId_IsNull() { regVillagePlayerId(CK_ISN, DOBJ); }

    /**
     * IsNotNull {is not null}. And OnlyOnceRegistered. <br>
     * VILLAGE_PLAYER_ID: {IX, INT UNSIGNED(10)}
     */
    public void setVillagePlayerId_IsNotNull() { regVillagePlayerId(CK_ISNN, DOBJ); }

    protected void regVillagePlayerId(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueVillagePlayerId(), "VILLAGE_PLAYER_ID"); }
    protected abstract ConditionValue xgetCValueVillagePlayerId();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_Equal(String messageContent) {
        doSetMessageContent_Equal(fRES(messageContent));
    }

    protected void doSetMessageContent_Equal(String messageContent) {
        regMessageContent(CK_EQ, messageContent);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_NotEqual(String messageContent) {
        doSetMessageContent_NotEqual(fRES(messageContent));
    }

    protected void doSetMessageContent_NotEqual(String messageContent) {
        regMessageContent(CK_NES, messageContent);
    }

    /**
     * GreaterThan(&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as greaterThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_GreaterThan(String messageContent) {
        regMessageContent(CK_GT, fRES(messageContent));
    }

    /**
     * LessThan(&lt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as lessThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_LessThan(String messageContent) {
        regMessageContent(CK_LT, fRES(messageContent));
    }

    /**
     * GreaterEqual(&gt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as greaterEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_GreaterEqual(String messageContent) {
        regMessageContent(CK_GE, fRES(messageContent));
    }

    /**
     * LessEqual(&lt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as lessEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_LessEqual(String messageContent) {
        regMessageContent(CK_LE, fRES(messageContent));
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContentList The collection of messageContent as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_InScope(Collection<String> messageContentList) {
        doSetMessageContent_InScope(messageContentList);
    }

    protected void doSetMessageContent_InScope(Collection<String> messageContentList) {
        regINS(CK_INS, cTL(messageContentList), xgetCValueMessageContent(), "MESSAGE_CONTENT");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContentList The collection of messageContent as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageContent_NotInScope(Collection<String> messageContentList) {
        doSetMessageContent_NotInScope(messageContentList);
    }

    protected void doSetMessageContent_NotInScope(Collection<String> messageContentList) {
        regINS(CK_NINS, cTL(messageContentList), xgetCValueMessageContent(), "MESSAGE_CONTENT");
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)} <br>
     * <pre>e.g. setMessageContent_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param messageContent The value of messageContent as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setMessageContent_LikeSearch(String messageContent, ConditionOptionCall<LikeSearchOption> opLambda) {
        setMessageContent_LikeSearch(messageContent, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)} <br>
     * <pre>e.g. setMessageContent_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param messageContent The value of messageContent as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setMessageContent_LikeSearch(String messageContent, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(messageContent), xgetCValueMessageContent(), "MESSAGE_CONTENT", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setMessageContent_NotLikeSearch(String messageContent, ConditionOptionCall<LikeSearchOption> opLambda) {
        setMessageContent_NotLikeSearch(messageContent, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * MESSAGE_CONTENT: {NotNull, VARCHAR(10000)}
     * @param messageContent The value of messageContent as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setMessageContent_NotLikeSearch(String messageContent, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(messageContent), xgetCValueMessageContent(), "MESSAGE_CONTENT", likeSearchOption);
    }

    protected void regMessageContent(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueMessageContent(), "MESSAGE_CONTENT"); }
    protected abstract ConditionValue xgetCValueMessageContent();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * @param messageDatetime The value of messageDatetime as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageDatetime_Equal(java.time.LocalDateTime messageDatetime) {
        regMessageDatetime(CK_EQ,  messageDatetime);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * @param messageDatetime The value of messageDatetime as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageDatetime_GreaterThan(java.time.LocalDateTime messageDatetime) {
        regMessageDatetime(CK_GT,  messageDatetime);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * @param messageDatetime The value of messageDatetime as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageDatetime_LessThan(java.time.LocalDateTime messageDatetime) {
        regMessageDatetime(CK_LT,  messageDatetime);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * @param messageDatetime The value of messageDatetime as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageDatetime_GreaterEqual(java.time.LocalDateTime messageDatetime) {
        regMessageDatetime(CK_GE,  messageDatetime);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * @param messageDatetime The value of messageDatetime as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageDatetime_LessEqual(java.time.LocalDateTime messageDatetime) {
        regMessageDatetime(CK_LE, messageDatetime);
    }

    /**
     * FromTo with various options. (versatile) {(default) fromDatetime &lt;= column &lt;= toDatetime} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * <pre>e.g. setMessageDatetime_FromTo(fromDate, toDate, op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">compareAsDate()</span>);</pre>
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of messageDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of messageDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of from-to. (NotNull)
     */
    public void setMessageDatetime_FromTo(java.time.LocalDateTime fromDatetime, java.time.LocalDateTime toDatetime, ConditionOptionCall<FromToOption> opLambda) {
        setMessageDatetime_FromTo(fromDatetime, toDatetime, xcFTOP(opLambda));
    }

    /**
     * FromTo with various options. (versatile) {(default) fromDatetime &lt;= column &lt;= toDatetime} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_DATETIME: {NotNull, DATETIME(19)}
     * <pre>e.g. setMessageDatetime_FromTo(fromDate, toDate, new <span style="color: #CC4747">FromToOption</span>().compareAsDate());</pre>
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of messageDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of messageDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param fromToOption The option of from-to. (NotNull)
     */
    protected void setMessageDatetime_FromTo(java.time.LocalDateTime fromDatetime, java.time.LocalDateTime toDatetime, FromToOption fromToOption) {
        String nm = "MESSAGE_DATETIME"; FromToOption op = fromToOption;
        regFTQ(xfFTHD(fromDatetime, nm, op), xfFTHD(toDatetime, nm, op), xgetCValueMessageDatetime(), nm, op);
    }

    protected void regMessageDatetime(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueMessageDatetime(), "MESSAGE_DATETIME"); }
    protected abstract ConditionValue xgetCValueMessageDatetime();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilli The value of messageUnixtimestampMilli as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_Equal(Long messageUnixtimestampMilli) {
        doSetMessageUnixtimestampMilli_Equal(messageUnixtimestampMilli);
    }

    protected void doSetMessageUnixtimestampMilli_Equal(Long messageUnixtimestampMilli) {
        regMessageUnixtimestampMilli(CK_EQ, messageUnixtimestampMilli);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilli The value of messageUnixtimestampMilli as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_NotEqual(Long messageUnixtimestampMilli) {
        doSetMessageUnixtimestampMilli_NotEqual(messageUnixtimestampMilli);
    }

    protected void doSetMessageUnixtimestampMilli_NotEqual(Long messageUnixtimestampMilli) {
        regMessageUnixtimestampMilli(CK_NES, messageUnixtimestampMilli);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilli The value of messageUnixtimestampMilli as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_GreaterThan(Long messageUnixtimestampMilli) {
        regMessageUnixtimestampMilli(CK_GT, messageUnixtimestampMilli);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilli The value of messageUnixtimestampMilli as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_LessThan(Long messageUnixtimestampMilli) {
        regMessageUnixtimestampMilli(CK_LT, messageUnixtimestampMilli);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilli The value of messageUnixtimestampMilli as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_GreaterEqual(Long messageUnixtimestampMilli) {
        regMessageUnixtimestampMilli(CK_GE, messageUnixtimestampMilli);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilli The value of messageUnixtimestampMilli as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_LessEqual(Long messageUnixtimestampMilli) {
        regMessageUnixtimestampMilli(CK_LE, messageUnixtimestampMilli);
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param minNumber The min number of messageUnixtimestampMilli. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of messageUnixtimestampMilli. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of range-of. (NotNull)
     */
    public void setMessageUnixtimestampMilli_RangeOf(Long minNumber, Long maxNumber, ConditionOptionCall<RangeOfOption> opLambda) {
        setMessageUnixtimestampMilli_RangeOf(minNumber, maxNumber, xcROOP(opLambda));
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param minNumber The min number of messageUnixtimestampMilli. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of messageUnixtimestampMilli. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param rangeOfOption The option of range-of. (NotNull)
     */
    protected void setMessageUnixtimestampMilli_RangeOf(Long minNumber, Long maxNumber, RangeOfOption rangeOfOption) {
        regROO(minNumber, maxNumber, xgetCValueMessageUnixtimestampMilli(), "MESSAGE_UNIXTIMESTAMP_MILLI", rangeOfOption);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilliList The collection of messageUnixtimestampMilli as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_InScope(Collection<Long> messageUnixtimestampMilliList) {
        doSetMessageUnixtimestampMilli_InScope(messageUnixtimestampMilliList);
    }

    protected void doSetMessageUnixtimestampMilli_InScope(Collection<Long> messageUnixtimestampMilliList) {
        regINS(CK_INS, cTL(messageUnixtimestampMilliList), xgetCValueMessageUnixtimestampMilli(), "MESSAGE_UNIXTIMESTAMP_MILLI");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * MESSAGE_UNIXTIMESTAMP_MILLI: {IX, NotNull, BIGINT UNSIGNED(20)}
     * @param messageUnixtimestampMilliList The collection of messageUnixtimestampMilli as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setMessageUnixtimestampMilli_NotInScope(Collection<Long> messageUnixtimestampMilliList) {
        doSetMessageUnixtimestampMilli_NotInScope(messageUnixtimestampMilliList);
    }

    protected void doSetMessageUnixtimestampMilli_NotInScope(Collection<Long> messageUnixtimestampMilliList) {
        regINS(CK_NINS, cTL(messageUnixtimestampMilliList), xgetCValueMessageUnixtimestampMilli(), "MESSAGE_UNIXTIMESTAMP_MILLI");
    }

    protected void regMessageUnixtimestampMilli(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueMessageUnixtimestampMilli(), "MESSAGE_UNIXTIMESTAMP_MILLI"); }
    protected abstract ConditionValue xgetCValueMessageUnixtimestampMilli();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * IS_STRONG: {NotNull, BIT}
     * @param isStrong The value of isStrong as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setIsStrong_Equal(Boolean isStrong) {
        regIsStrong(CK_EQ, isStrong);
    }

    protected void regIsStrong(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueIsStrong(), "IS_STRONG"); }
    protected abstract ConditionValue xgetCValueIsStrong();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * @param registerDatetime The value of registerDatetime as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setRegisterDatetime_Equal(java.time.LocalDateTime registerDatetime) {
        regRegisterDatetime(CK_EQ,  registerDatetime);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * @param registerDatetime The value of registerDatetime as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setRegisterDatetime_GreaterThan(java.time.LocalDateTime registerDatetime) {
        regRegisterDatetime(CK_GT,  registerDatetime);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * @param registerDatetime The value of registerDatetime as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setRegisterDatetime_LessThan(java.time.LocalDateTime registerDatetime) {
        regRegisterDatetime(CK_LT,  registerDatetime);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * @param registerDatetime The value of registerDatetime as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setRegisterDatetime_GreaterEqual(java.time.LocalDateTime registerDatetime) {
        regRegisterDatetime(CK_GE,  registerDatetime);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * @param registerDatetime The value of registerDatetime as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setRegisterDatetime_LessEqual(java.time.LocalDateTime registerDatetime) {
        regRegisterDatetime(CK_LE, registerDatetime);
    }

    /**
     * FromTo with various options. (versatile) {(default) fromDatetime &lt;= column &lt;= toDatetime} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * <pre>e.g. setRegisterDatetime_FromTo(fromDate, toDate, op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">compareAsDate()</span>);</pre>
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of registerDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of registerDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of from-to. (NotNull)
     */
    public void setRegisterDatetime_FromTo(java.time.LocalDateTime fromDatetime, java.time.LocalDateTime toDatetime, ConditionOptionCall<FromToOption> opLambda) {
        setRegisterDatetime_FromTo(fromDatetime, toDatetime, xcFTOP(opLambda));
    }

    /**
     * FromTo with various options. (versatile) {(default) fromDatetime &lt;= column &lt;= toDatetime} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * REGISTER_DATETIME: {NotNull, DATETIME(19)}
     * <pre>e.g. setRegisterDatetime_FromTo(fromDate, toDate, new <span style="color: #CC4747">FromToOption</span>().compareAsDate());</pre>
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of registerDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of registerDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param fromToOption The option of from-to. (NotNull)
     */
    protected void setRegisterDatetime_FromTo(java.time.LocalDateTime fromDatetime, java.time.LocalDateTime toDatetime, FromToOption fromToOption) {
        String nm = "REGISTER_DATETIME"; FromToOption op = fromToOption;
        regFTQ(xfFTHD(fromDatetime, nm, op), xfFTHD(toDatetime, nm, op), xgetCValueRegisterDatetime(), nm, op);
    }

    protected void regRegisterDatetime(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueRegisterDatetime(), "REGISTER_DATETIME"); }
    protected abstract ConditionValue xgetCValueRegisterDatetime();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_Equal(String registerTrace) {
        doSetRegisterTrace_Equal(fRES(registerTrace));
    }

    protected void doSetRegisterTrace_Equal(String registerTrace) {
        regRegisterTrace(CK_EQ, registerTrace);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_NotEqual(String registerTrace) {
        doSetRegisterTrace_NotEqual(fRES(registerTrace));
    }

    protected void doSetRegisterTrace_NotEqual(String registerTrace) {
        regRegisterTrace(CK_NES, registerTrace);
    }

    /**
     * GreaterThan(&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as greaterThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_GreaterThan(String registerTrace) {
        regRegisterTrace(CK_GT, fRES(registerTrace));
    }

    /**
     * LessThan(&lt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as lessThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_LessThan(String registerTrace) {
        regRegisterTrace(CK_LT, fRES(registerTrace));
    }

    /**
     * GreaterEqual(&gt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as greaterEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_GreaterEqual(String registerTrace) {
        regRegisterTrace(CK_GE, fRES(registerTrace));
    }

    /**
     * LessEqual(&lt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as lessEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_LessEqual(String registerTrace) {
        regRegisterTrace(CK_LE, fRES(registerTrace));
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTraceList The collection of registerTrace as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_InScope(Collection<String> registerTraceList) {
        doSetRegisterTrace_InScope(registerTraceList);
    }

    protected void doSetRegisterTrace_InScope(Collection<String> registerTraceList) {
        regINS(CK_INS, cTL(registerTraceList), xgetCValueRegisterTrace(), "REGISTER_TRACE");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTraceList The collection of registerTrace as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setRegisterTrace_NotInScope(Collection<String> registerTraceList) {
        doSetRegisterTrace_NotInScope(registerTraceList);
    }

    protected void doSetRegisterTrace_NotInScope(Collection<String> registerTraceList) {
        regINS(CK_NINS, cTL(registerTraceList), xgetCValueRegisterTrace(), "REGISTER_TRACE");
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)} <br>
     * <pre>e.g. setRegisterTrace_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param registerTrace The value of registerTrace as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setRegisterTrace_LikeSearch(String registerTrace, ConditionOptionCall<LikeSearchOption> opLambda) {
        setRegisterTrace_LikeSearch(registerTrace, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)} <br>
     * <pre>e.g. setRegisterTrace_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param registerTrace The value of registerTrace as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setRegisterTrace_LikeSearch(String registerTrace, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(registerTrace), xgetCValueRegisterTrace(), "REGISTER_TRACE", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setRegisterTrace_NotLikeSearch(String registerTrace, ConditionOptionCall<LikeSearchOption> opLambda) {
        setRegisterTrace_NotLikeSearch(registerTrace, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * REGISTER_TRACE: {NotNull, VARCHAR(64)}
     * @param registerTrace The value of registerTrace as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setRegisterTrace_NotLikeSearch(String registerTrace, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(registerTrace), xgetCValueRegisterTrace(), "REGISTER_TRACE", likeSearchOption);
    }

    protected void regRegisterTrace(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueRegisterTrace(), "REGISTER_TRACE"); }
    protected abstract ConditionValue xgetCValueRegisterTrace();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * @param updateDatetime The value of updateDatetime as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setUpdateDatetime_Equal(java.time.LocalDateTime updateDatetime) {
        regUpdateDatetime(CK_EQ,  updateDatetime);
    }

    /**
     * GreaterThan(&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * @param updateDatetime The value of updateDatetime as greaterThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setUpdateDatetime_GreaterThan(java.time.LocalDateTime updateDatetime) {
        regUpdateDatetime(CK_GT,  updateDatetime);
    }

    /**
     * LessThan(&lt;). And NullIgnored, OnlyOnceRegistered. <br>
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * @param updateDatetime The value of updateDatetime as lessThan. (basically NotNull: error as default, or no condition as option)
     */
    public void setUpdateDatetime_LessThan(java.time.LocalDateTime updateDatetime) {
        regUpdateDatetime(CK_LT,  updateDatetime);
    }

    /**
     * GreaterEqual(&gt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * @param updateDatetime The value of updateDatetime as greaterEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setUpdateDatetime_GreaterEqual(java.time.LocalDateTime updateDatetime) {
        regUpdateDatetime(CK_GE,  updateDatetime);
    }

    /**
     * LessEqual(&lt;=). And NullIgnored, OnlyOnceRegistered. <br>
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * @param updateDatetime The value of updateDatetime as lessEqual. (basically NotNull: error as default, or no condition as option)
     */
    public void setUpdateDatetime_LessEqual(java.time.LocalDateTime updateDatetime) {
        regUpdateDatetime(CK_LE, updateDatetime);
    }

    /**
     * FromTo with various options. (versatile) {(default) fromDatetime &lt;= column &lt;= toDatetime} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * <pre>e.g. setUpdateDatetime_FromTo(fromDate, toDate, op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">compareAsDate()</span>);</pre>
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of updateDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of updateDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of from-to. (NotNull)
     */
    public void setUpdateDatetime_FromTo(java.time.LocalDateTime fromDatetime, java.time.LocalDateTime toDatetime, ConditionOptionCall<FromToOption> opLambda) {
        setUpdateDatetime_FromTo(fromDatetime, toDatetime, xcFTOP(opLambda));
    }

    /**
     * FromTo with various options. (versatile) {(default) fromDatetime &lt;= column &lt;= toDatetime} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * UPDATE_DATETIME: {NotNull, DATETIME(19)}
     * <pre>e.g. setUpdateDatetime_FromTo(fromDate, toDate, new <span style="color: #CC4747">FromToOption</span>().compareAsDate());</pre>
     * @param fromDatetime The from-datetime(yyyy/MM/dd HH:mm:ss.SSS) of updateDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param toDatetime The to-datetime(yyyy/MM/dd HH:mm:ss.SSS) of updateDatetime. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param fromToOption The option of from-to. (NotNull)
     */
    protected void setUpdateDatetime_FromTo(java.time.LocalDateTime fromDatetime, java.time.LocalDateTime toDatetime, FromToOption fromToOption) {
        String nm = "UPDATE_DATETIME"; FromToOption op = fromToOption;
        regFTQ(xfFTHD(fromDatetime, nm, op), xfFTHD(toDatetime, nm, op), xgetCValueUpdateDatetime(), nm, op);
    }

    protected void regUpdateDatetime(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueUpdateDatetime(), "UPDATE_DATETIME"); }
    protected abstract ConditionValue xgetCValueUpdateDatetime();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_Equal(String updateTrace) {
        doSetUpdateTrace_Equal(fRES(updateTrace));
    }

    protected void doSetUpdateTrace_Equal(String updateTrace) {
        regUpdateTrace(CK_EQ, updateTrace);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_NotEqual(String updateTrace) {
        doSetUpdateTrace_NotEqual(fRES(updateTrace));
    }

    protected void doSetUpdateTrace_NotEqual(String updateTrace) {
        regUpdateTrace(CK_NES, updateTrace);
    }

    /**
     * GreaterThan(&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as greaterThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_GreaterThan(String updateTrace) {
        regUpdateTrace(CK_GT, fRES(updateTrace));
    }

    /**
     * LessThan(&lt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as lessThan. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_LessThan(String updateTrace) {
        regUpdateTrace(CK_LT, fRES(updateTrace));
    }

    /**
     * GreaterEqual(&gt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as greaterEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_GreaterEqual(String updateTrace) {
        regUpdateTrace(CK_GE, fRES(updateTrace));
    }

    /**
     * LessEqual(&lt;=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as lessEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_LessEqual(String updateTrace) {
        regUpdateTrace(CK_LE, fRES(updateTrace));
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTraceList The collection of updateTrace as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_InScope(Collection<String> updateTraceList) {
        doSetUpdateTrace_InScope(updateTraceList);
    }

    protected void doSetUpdateTrace_InScope(Collection<String> updateTraceList) {
        regINS(CK_INS, cTL(updateTraceList), xgetCValueUpdateTrace(), "UPDATE_TRACE");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTraceList The collection of updateTrace as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUpdateTrace_NotInScope(Collection<String> updateTraceList) {
        doSetUpdateTrace_NotInScope(updateTraceList);
    }

    protected void doSetUpdateTrace_NotInScope(Collection<String> updateTraceList) {
        regINS(CK_NINS, cTL(updateTraceList), xgetCValueUpdateTrace(), "UPDATE_TRACE");
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)} <br>
     * <pre>e.g. setUpdateTrace_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param updateTrace The value of updateTrace as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setUpdateTrace_LikeSearch(String updateTrace, ConditionOptionCall<LikeSearchOption> opLambda) {
        setUpdateTrace_LikeSearch(updateTrace, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)} <br>
     * <pre>e.g. setUpdateTrace_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param updateTrace The value of updateTrace as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setUpdateTrace_LikeSearch(String updateTrace, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(updateTrace), xgetCValueUpdateTrace(), "UPDATE_TRACE", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setUpdateTrace_NotLikeSearch(String updateTrace, ConditionOptionCall<LikeSearchOption> opLambda) {
        setUpdateTrace_NotLikeSearch(updateTrace, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * UPDATE_TRACE: {NotNull, VARCHAR(64)}
     * @param updateTrace The value of updateTrace as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setUpdateTrace_NotLikeSearch(String updateTrace, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(updateTrace), xgetCValueUpdateTrace(), "UPDATE_TRACE", likeSearchOption);
    }

    protected void regUpdateTrace(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueUpdateTrace(), "UPDATE_TRACE"); }
    protected abstract ConditionValue xgetCValueUpdateTrace();

    // ===================================================================================
    //                                                                     ScalarCondition
    //                                                                     ===============
    /**
     * Prepare ScalarCondition as equal. <br>
     * {where FOO = (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<MessageCB> scalar_Equal() {
        return xcreateSLCFunction(CK_EQ, MessageCB.class);
    }

    /**
     * Prepare ScalarCondition as equal. <br>
     * {where FOO &lt;&gt; (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<MessageCB> scalar_NotEqual() {
        return xcreateSLCFunction(CK_NES, MessageCB.class);
    }

    /**
     * Prepare ScalarCondition as greaterThan. <br>
     * {where FOO &gt; (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<MessageCB> scalar_GreaterThan() {
        return xcreateSLCFunction(CK_GT, MessageCB.class);
    }

    /**
     * Prepare ScalarCondition as lessThan. <br>
     * {where FOO &lt; (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<MessageCB> scalar_LessThan() {
        return xcreateSLCFunction(CK_LT, MessageCB.class);
    }

    /**
     * Prepare ScalarCondition as greaterEqual. <br>
     * {where FOO &gt;= (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<MessageCB> scalar_GreaterEqual() {
        return xcreateSLCFunction(CK_GE, MessageCB.class);
    }

    /**
     * Prepare ScalarCondition as lessEqual. <br>
     * {where FOO &lt;= (select max(BAR) from ...)}
     * <pre>
     * cb.query().<span style="color: #CC4747">scalar_LessEqual()</span>.max(new SubQuery&lt;MessageCB&gt;() {
     *     public void query(MessageCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<MessageCB> scalar_LessEqual() {
        return xcreateSLCFunction(CK_LE, MessageCB.class);
    }

    @SuppressWarnings("unchecked")
    protected <CB extends ConditionBean> void xscalarCondition(String fn, SubQuery<CB> sq, String rd, HpSLCCustomized<CB> cs, ScalarConditionOption op) {
        assertObjectNotNull("subQuery", sq);
        MessageCB cb = xcreateScalarConditionCB(); sq.query((CB)cb);
        String pp = keepScalarCondition(cb.query()); // for saving query-value
        cs.setPartitionByCBean((CB)xcreateScalarConditionPartitionByCB()); // for using partition-by
        registerScalarCondition(fn, cb.query(), pp, rd, cs, op);
    }
    public abstract String keepScalarCondition(MessageCQ sq);

    protected MessageCB xcreateScalarConditionCB() {
        MessageCB cb = newMyCB(); cb.xsetupForScalarCondition(this); return cb;
    }

    protected MessageCB xcreateScalarConditionPartitionByCB() {
        MessageCB cb = newMyCB(); cb.xsetupForScalarConditionPartitionBy(this); return cb;
    }

    // ===================================================================================
    //                                                                        Manual Order
    //                                                                        ============
    /**
     * Order along manual ordering information.
     * <pre>
     * cb.query().addOrderBy_Birthdate_Asc().<span style="color: #CC4747">withManualOrder</span>(<span style="color: #553000">op</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_GreaterEqual</span>(priorityDate); <span style="color: #3F7E5E">// e.g. 2000/01/01</span>
     * });
     * <span style="color: #3F7E5E">// order by </span>
     * <span style="color: #3F7E5E">//   case</span>
     * <span style="color: #3F7E5E">//     when BIRTHDATE &gt;= '2000/01/01' then 0</span>
     * <span style="color: #3F7E5E">//     else 1</span>
     * <span style="color: #3F7E5E">//   end asc, ...</span>
     *
     * cb.query().addOrderBy_MemberStatusCode_Asc().<span style="color: #CC4747">withManualOrder</span>(<span style="color: #553000">op</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_Equal</span>(CDef.MemberStatus.Withdrawal);
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_Equal</span>(CDef.MemberStatus.Formalized);
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_Equal</span>(CDef.MemberStatus.Provisional);
     * });
     * <span style="color: #3F7E5E">// order by </span>
     * <span style="color: #3F7E5E">//   case</span>
     * <span style="color: #3F7E5E">//     when MEMBER_STATUS_CODE = 'WDL' then 0</span>
     * <span style="color: #3F7E5E">//     when MEMBER_STATUS_CODE = 'FML' then 1</span>
     * <span style="color: #3F7E5E">//     when MEMBER_STATUS_CODE = 'PRV' then 2</span>
     * <span style="color: #3F7E5E">//     else 3</span>
     * <span style="color: #3F7E5E">//   end asc, ...</span>
     * </pre>
     * <p>This function with Union is unsupported!</p>
     * <p>The order values are bound (treated as bind parameter).</p>
     * @param opLambda The callback for option of manual-order containing order values. (NotNull)
     */
    public void withManualOrder(ManualOrderOptionCall opLambda) { // is user public!
        xdoWithManualOrder(cMOO(opLambda));
    }

    // ===================================================================================
    //                                                                    Small Adjustment
    //                                                                    ================
    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    protected MessageCB newMyCB() {
        return new MessageCB();
    }
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xabUDT() { return Date.class.getName(); }
    protected String xabCQ() { return MessageCQ.class.getName(); }
    protected String xabLSO() { return LikeSearchOption.class.getName(); }
    protected String xabSLCS() { return HpSLCSetupper.class.getName(); }
    protected String xabSCP() { return SubQuery.class.getName(); }
}
