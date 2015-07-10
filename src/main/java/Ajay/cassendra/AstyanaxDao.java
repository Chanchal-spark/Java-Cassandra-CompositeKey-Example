package Ajay.cassendra;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class AstyanaxDao {
    private static final Logger LOG = LoggerFactory.getLogger(AstyanaxDao.class);
    private Keyspace keyspace;
    private AstyanaxContext<Keyspace> astyanaxContext;

    public AstyanaxDao(String host, String keyspace) {
        try {
            this.astyanaxContext = new AstyanaxContext.Builder()
                    .forCluster("Test Cluster")
                    .forKeyspace(keyspace)
                    .withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.NONE))
                    .withConnectionPoolConfiguration(
                            new ConnectionPoolConfigurationImpl("MyConnectionPool").setMaxConnsPerHost(1)
                                    .setSeeds(host)).withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                    .buildKeyspace(ThriftFamilyFactory.getInstance());

            this.astyanaxContext.start();
            this.keyspace = this.astyanaxContext.getEntity();
            // test the connection
            this.keyspace.describeKeyspace();
        } catch (Throwable e) {
            LOG.warn("Preparation failed.", e);
            throw new RuntimeException("Failed to prepare CassandraBolt", e);
        }
    }

    public void cleanup() {
        this.astyanaxContext.shutdown();
    }

    /**
     * Writes columns.
     */
    public void write(String columnFamilyName, String rowKey, Map<String, String> columns) throws ConnectionException {
        MutationBatch mutation = keyspace.prepareMutationBatch();
        ColumnFamily<String, String> columnFamily = new ColumnFamily<String, String>(columnFamilyName,
                StringSerializer.get(), StringSerializer.get());
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            mutation.withRow(columnFamily, rowKey).putColumn(entry.getKey(), entry.getValue(), null);
        }
        mutation.execute();
    }

    /**
     * Writes compound/composite columns.
     */
    public void writeBlog(String columnFamilyName, String rowKey, Stock blog, String value) throws ConnectionException {
        AnnotatedCompositeSerializer<Stock> entitySerializer = new AnnotatedCompositeSerializer<Stock>(Stock.class);
        MutationBatch mutation = keyspace.prepareMutationBatch();
        ColumnFamily<String, Stock> columnFamily = new ColumnFamily<String, Stock>(columnFamilyName,
                StringSerializer.get(), entitySerializer);
        mutation.withRow(columnFamily, rowKey).putColumn(blog, value, null);
        mutation.execute();
    }


}
