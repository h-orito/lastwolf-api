package com.ort.dbflute.bsbhv.loader;

import java.util.List;

import org.dbflute.bhv.*;
import com.ort.dbflute.exbhv.*;
import com.ort.dbflute.exentity.*;

/**
 * The referrer loader of MESSAGE_TYPE as TABLE. <br>
 * <pre>
 * [primary key]
 *     MESSAGE_TYPE_CODE
 *
 * [column]
 *     MESSAGE_TYPE_CODE, MESSAGE_TYPE_NAME
 *
 * [sequence]
 *     
 *
 * [identity]
 *     
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
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public class LoaderOfMessageType {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected List<MessageType> _selectedList;
    protected BehaviorSelector _selector;
    protected MessageTypeBhv _myBhv; // lazy-loaded

    // ===================================================================================
    //                                                                   Ready for Loading
    //                                                                   =================
    public LoaderOfMessageType ready(List<MessageType> selectedList, BehaviorSelector selector)
    { _selectedList = selectedList; _selector = selector; return this; }

    protected MessageTypeBhv myBhv()
    { if (_myBhv != null) { return _myBhv; } else { _myBhv = _selector.select(MessageTypeBhv.class); return _myBhv; } }

    // ===================================================================================
    //                                                                    Pull out Foreign
    //                                                                    ================
    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public List<MessageType> getSelectedList() { return _selectedList; }
    public BehaviorSelector getSelector() { return _selector; }
}
