package Ajay.cassendra;

import com.netflix.astyanax.annotations.Component;

public class Stock {
    @Component(ordinal = 0)
    public String location;
    @Component(ordinal = 1)
    public String stocktype ;
    @Component(ordinal = 2)
    public String field ;

    public Stock() {
    }
}