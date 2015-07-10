package Ajay.cassendra;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;

public class AstyanaxDaoTest {
    private static Logger LOG = LoggerFactory.getLogger(AstyanaxDaoTest.class);

   /* @Test
    public void testDao() throws Exception {
       AstyanaxDao dao = new AstyanaxDao("localhost:9160", "examples");
       dumpStrings(dao.read("stocks", "boneill42"));
    }*/

    @Test
    public void teststockDao() throws Exception {
        long now = System.currentTimeMillis();
       AstyanaxDao dao = new AstyanaxDao("localhost:9160", "inventory");
       Stock stock = new Stock();
       stock.location="USA";
       stock.stocktype="Anytype";
       stock.field="qty";


       dao.writeBlog("stock", "10", stock,"1000");
 ;


}
}
