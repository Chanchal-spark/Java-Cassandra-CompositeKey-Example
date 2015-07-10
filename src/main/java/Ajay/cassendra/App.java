package Ajay.cassendra;

import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws ConnectionException
    {
    	  long now = System.currentTimeMillis();
          AstyanaxDao dao = new AstyanaxDao("localhost:9160", "inventory");
          Stock stock = new Stock();
          stock.location="USA";
          stock.stocktype="stocktype";
          stock.field="qty";


          dao.writeBlog("stock", "6", stock,"1000");
          System.out.println("successfuly inserted");
    }
}
