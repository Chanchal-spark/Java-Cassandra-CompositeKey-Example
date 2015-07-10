package Chanchal.Cassendra;

import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class PopulationAPP {

	private static AnnotatedCompositeSerializer<Population> compSerializer =
		    new AnnotatedCompositeSerializer<Population>(Population.class);

		private static ColumnFamily<Integer, Population> CF_POPULATION =
		    new ColumnFamily<Integer, Population>("population",
		                                           IntegerSerializer.get(),
		                                           compSerializer,
		                                           IntegerSerializer.get());

		private static Population NewYork = new Population("NY", "New York", 10000);
		private static Population SanDiego = new Population("CA", "San Diego", 20000);
		private static Population SanFrancisco = new Population("CA", "San Francisco", 30000);
		private static Population Seattle = new Population("WA", "Seattle", 40000);

	public static void main(String[] args) throws ConnectionException {

		AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
				.forCluster("Test Cluster")
				.forKeyspace("chanchal1")
				.withAstyanaxConfiguration(
						new AstyanaxConfigurationImpl().setDiscoveryType(
								NodeDiscoveryType.RING_DESCRIBE)
								.setConnectionPoolType(
										ConnectionPoolType.TOKEN_AWARE))
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl("MyConnectionPool")
								.setPort(9160).setMaxConnsPerHost(10)
								.setSeeds("127.0.0.1:9160"))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		Keyspace keyspace = context.getClient();
		keyspace.createColumnFamily(CF_POPULATION,null);
		MutationBatch m = keyspace.prepareMutationBatch();

		Random random = new Random();

		for (int year = 2011; year <= 2014; year++) {

		    m.withRow(CF_POPULATION, year)
		        .putColumn(NewYork.clone(), random.nextInt(25000))
		        .putColumn(SanDiego.clone(), random.nextInt(25000))
		        .putColumn(SanFrancisco.clone(), random.nextInt(25000))
		        .putColumn(Seattle.clone(), random.nextInt(25000));
		}

		m.execute();
		System.out.println("successfully build");
	}
}
