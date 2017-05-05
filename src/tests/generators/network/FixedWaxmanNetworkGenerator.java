package tests.generators.network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import mulavito.graph.generators.WaxmanGraphGenerator;
import tests.generators.GeneratorParameter;
import vnreal.core.oldFramework.ConversionHelper;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * This is the fixed WaxmanNetwork generator which is using fixed seed
 * 
 * @author Fabian Kokot
 *
 */
@GeneratorParameter(
		parameters = { "TR:Waxman_alpha", "TR:Waxman_beta" , "TR:SNetSize", "TR:NumVNodesPerNet", "TR:NumVNets", "Seed:Seed"}
) 
public class FixedWaxmanNetworkGenerator extends AbstractNetworkStackGenerator{
	@Override
	public NetworkStack generate(ArrayList<Object> parameters) {
		Double alpha = (Double) parameters.get(0);
		Double beta = (Double) parameters.get(1);
		Integer sNetSize = ConversionHelper.paramObjectToInteger(parameters.get(2));
		Integer numVNodesPerNet = ConversionHelper.paramObjectToInteger(parameters.get(3));
		Integer numVNets = ConversionHelper.paramObjectToInteger(parameters.get(4));
		Long seed = ConversionHelper.paramObjectToLong(parameters.get(5));
		
		Random random = seed == null ? new Random() : new Random(seed);
		SubstrateNetwork sn = createSubstrateNetwork(alpha, beta, sNetSize, random);
		
		List<VirtualNetwork> vns = createVirtualNetworks(alpha, beta, numVNets, numVNodesPerNet, random);
		
		NetworkStack ns = new NetworkStack(sn, vns);
		
		
		return ns;
	}


	/**
	 * This method creates the substrateNEtwork
	 * 
	 * @param alpha Alpha value
	 * @param beta  Beta value
	 * @param bidirectEdges True if edges should be bidrectional
	 * @param sNetSize size of the SubstrateNetwork
	 * @return
	 */
	protected SubstrateNetwork createSubstrateNetwork(Double alpha, Double beta, Integer sNetSize, Random random) {
		
		WaxmanGraphGenerator<SubstrateNode, SubstrateLink> sGenerator = new WaxmanGraphGenerator<SubstrateNode, SubstrateLink>(
				random, alpha, beta, false);
		
		SubstrateNetwork sNetwork = new SubstrateNetwork(false);
		for (int i = 0; i < sNetSize; ++i) {
			SubstrateNode sn = new SubstrateNode();
			sn.setName(sn.getId() + "");
			sNetwork.addVertex(sn);
		}
		
		sGenerator.generate(sNetwork);

		
		return sNetwork;
	}
	
	
	/**
	 * This method creates the virtual networks
	 * 
	 * @param alpha
	 * @param beta
	 * @param numVNets
	 * @param numVNodesPerNet
	 * @return
	 */
	protected List<VirtualNetwork> createVirtualNetworks(Double alpha,
			Double beta, Integer numVNets, Integer numVNodesPerNet, Random random) {
		WaxmanGraphGenerator<VirtualNode, VirtualLink> vGenerator = new WaxmanGraphGenerator<VirtualNode, VirtualLink>(
				random,
				alpha, beta, false);
		
		
		List<VirtualNetwork> vNetworks = new LinkedList<VirtualNetwork>();
		for (int i = 1; i <= numVNets; ++i) {
			VirtualNetwork vNetwork = new VirtualNetwork(i);
			vNetwork.setName(i + "");
			for (int n = 0; n < numVNodesPerNet; ++n) {
				VirtualNode vn = new VirtualNode(i);

				vNetwork.addVertex(vn);
			}
			vGenerator.generate(vNetwork);
			
			vNetworks.add(vNetwork);
		}
		
		return vNetworks;
	}


	@Override
	public void reset() {
	}
}
