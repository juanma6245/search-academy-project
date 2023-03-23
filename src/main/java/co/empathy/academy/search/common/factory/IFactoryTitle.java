package co.empathy.academy.search.common.factory;

import co.empathy.academy.search.common.TSVtype;
import co.empathy.academy.search.model.title.Title;

public interface IFactoryTitle {
    Title getTitle(TSVtype type, String[] data);
}
