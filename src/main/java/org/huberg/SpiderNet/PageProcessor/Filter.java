package org.huberg.SpiderNet.PageProcessor;

import org.huberg.SpiderNet.Page;

/**
 * Created by admin on 2015/6/16.
 */
public interface Filter {

    Page filter(Page page);

    boolean checkParameter();
}
