package co.empathy.academy.search.common.factory;

import co.empathy.academy.search.common.CSVtype;
import co.empathy.academy.search.model.title.Title;

public interface IFactoryTitle {
    Title getTitle(CSVtype type, Object[] data);
}
