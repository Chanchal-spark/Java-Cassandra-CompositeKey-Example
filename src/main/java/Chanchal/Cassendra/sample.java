package Chanchal.Cassendra;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class sample {

    public static void main(String[] args) throws Exception{
    AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
    .forCluster("Test Cluster")
    .forKeyspace("chanchal")
    .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                    .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
                    .setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE)
    )
    .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
        .setPort(9160)
        .setMaxConnsPerHost(10)
        .setSeeds("http://127.0.0.1:9160")
    )
    .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
    .buildKeyspace(ThriftFamilyFactory.getInstance());

    context.start();
    Keyspace keyspace = context.getEntity();

    ColumnFamily<String, String> CF_USER_INFO =
              new ColumnFamily<String, String>(
                "Standard1",              // Column Family Name
                StringSerializer.get(),   // Key Serializer
                StringSerializer.get());  // Column Serializer

    // Inserting data
    MutationBatch m = keyspace.prepareMutationBatch();

    m.withRow(CF_USER_INFO, "acct1234")
      .putColumn("firstname", "john", null)
      .putColumn("lastname", "smith", null)
      .putColumn("address", "555 Elm St", null)
      .putColumn("age", 30, null);

    m.withRow(CF_USER_INFO, "acct1234")
      .incrementCounterColumn("loginCount", 1);

    try {
      OperationResult<Void> result = m.execute();
    } catch (ConnectionException e) {
    }

    System.out.println("completed the task!!!");

    OperationResult<ColumnList<String>> result =
              keyspace.prepareQuery(CF_USER_INFO)
                .getKey("Key1")
                .execute();
            ColumnList<String> columns = result.getResult();

            // Lookup columns in response by name
            int age        = columns.getColumnByName("age").getIntegerValue();
            long counter   = columns.getColumnByName("loginCount").getLongValue();
            String address = columns.getColumnByName("address").getStringValue();

            // Or, iterate through the columns
            for (Column<String> c : result.getResult()) {
              System.out.println(c.getName());
            }
    }

}