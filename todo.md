# TODO

* query part
** report - not unique emails (due to async)
** report - cross bounded context
** report - nontrivial calculation ( modficiation counts in hour per ID, the most active originator)


* uniqnes & refactor in JPA (Spring Data) implementation

* upcast deprecated subdir
    
    

* pagination / scrollable during events fetch (larg number of events / out of mem)
* UniqueValuesStore - need to handle removal and rename in public API
* snapshot as async
* comparision of effectiveness of fetch with / without snapshot
* applying event buss 
* apply diagram (few levels of detail)
* saga example
* email notification sender where is should be invoked?
* ContractBroken vs. DomainOperation Exceptions coexistence 
* BDD like testing (black box)

* articles to read:
    * http://cqrs.nu/Faq
    * https://www.authorea.com/users/13936/articles/86622-event-sourcing-design-pattern-in-a-java-enterprise-application/_show_article#axon__minus__docs-landing-welcome

* move mavenm to http://mvnvm.org/
