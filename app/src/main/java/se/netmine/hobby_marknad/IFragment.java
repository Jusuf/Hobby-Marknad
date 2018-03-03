package se.netmine.hobby_marknad;

/**
 * Created by jusuf on 2017-07-10.
 */

public interface IFragment{
    void setTitle();

    void onFaqsUpdated(Faq[] faqs);

    void onDealersUpdated(Dealer[] dealers);

    void onCampingsUpdated(CampingMin[] campings);
}
