#include "ns3/applications-module.h"
#include "ns3/core-module.h"
#include "ns3/flow-monitor-module.h"
#include "ns3/internet-module.h"
#include "ns3/network-module.h"
#include "ns3/point-to-point-module.h"
#include "ns3/traffic-control-module.h"


using namespace ns3;

NS_LOG_COMPONENT_DEFINE("TrafficControlExample");

int
main(int argc, char* argv[])
{
    double simulationTime = 10; // seconds
    std::string transportProt = "Udp";
    std::string socketType;

    CommandLine cmd(__FILE__);
    //cmd.AddValue("transportProt", "Transport protocol to use: Tcp, Udp", transportProt);
    cmd.Parse(argc, argv);

    if (transportProt == "Tcp")
    {
        socketType = "ns3::TcpSocketFactory";
    }
    else
    {
        socketType = "ns3::UdpSocketFactory";
    }

    NodeContainer nodes;
    nodes.Create(3);

    InternetStackHelper stack;
    stack.Install(nodes);

    PointToPointHelper pointToPoint;
    pointToPoint.SetDeviceAttribute("DataRate", StringValue("10Mbps"));
    pointToPoint.SetChannelAttribute("Delay", StringValue("2ms"));
    pointToPoint.SetQueue("ns3::DropTailQueue", "MaxSize", StringValue("1p"));

    NetDeviceContainer devices01;
    devices01 = pointToPoint.Install(nodes.Get(0),nodes.Get(1));

    NetDeviceContainer devices12;
    devices12 = pointToPoint.Install(nodes.Get(1),nodes.Get(2));

    
    Ipv4AddressHelper address01;
    address01.SetBase("10.1.1.0", "255.255.255.0");
    Ipv4InterfaceContainer interfaces01 = address01.Assign(devices01);

    Ipv4AddressHelper address12;
    address12.SetB12ase("10.1.2.0", "255.255.255.0");
    Ipv4InterfaceContainer interfaces12 = address12.Assign(devices12);

    Ipv4GlobalRoutingHelper::PopulateRoutingTables();

    // Flow
    uint16_t port = 7;
    Address localAddress(InetSocketAddress(Ipv4Address::GetAny(), port));
    PacketSinkHelper packetSinkHelper("ns3::UdpSocketFactory", localAddress);
    ApplicationContainer sinkApp = packetSinkHelper.Install(nodes.Get(2));

    sinkApp.Start(Seconds(0.0));
    sinkApp.Stop(Seconds(simulationTime + 0.1));

    uint32_t payloadSize = 1448;
    Config::SetDefault("ns3::TcpSocket::SegmentSize", UintegerValue(payloadSize));

    OnOffHelper onoff("ns3::UdpSocketFactory", Ipv4Address::GetAny());
    onoff.SetAttribute("OnTime", StringValue("ns3::ConstantRandomVariable[Constant=1]"));
    onoff.SetAttribute("OffTime", StringValue("ns3::ConstantRandomVariable[Constant=0]"));
    onoff.SetAttribute("PacketSize", UintegerValue(payloadSize));
    onoff.SetAttribute("DataRate", StringValue("50Mbps")); // bit/s
    ApplicationContainer apps;

    InetSocketAddress rmt(interfaces12.GetAddress(1), port);
    rmt.SetTos(0xb8);
    AddressValue remoteAddress(rmt);
    onoff.SetAttribute("Remote", remoteAddress);
    apps.Add(onoff.Install(nodes.Get(0)));
    apps.Start(Seconds(1.0));
    apps.Stop(Seconds(simulationTime + 0.1));

    FlowMonitorHelper flowmon;
    Ptr<FlowMonitor> monitor = flowmon.InstallAll();

    Simulator::Stop(Seconds(simulationTime + 5));
    Simulator::Run();

    Ptr<Ipv4FlowClassifier> classifier = DynamicCast<Ipv4FlowClassifier>(flowmon.GetClassifier());
    std::map<FlowId, FlowMonitor::FlowStats> stats = monitor->GetFlowStats();
    std::cout << std::endl << "*** Flow monitor statistics ***" << std::endl;
    std::cout << "  Tx Packets/Bytes:   " << stats[1].txPackets << " / " << stats[1].txBytes
              << std::endl;
    std::cout << "  Offered Load: "
              << stats[1].txBytes * 8.0 /
                     (stats[1].timeLastTxPacket.GetSeconds() -
                      stats[1].timeFirstTxPacket.GetSeconds()) /
                     1000000
              << " Mbps" << std::endl;
    std::cout << "  Rx Packets/Bytes:   " << stats[1].rxPackets << " / " << stats[1].rxBytes
              << std::endl;
           
    Simulator::Destroy();

    
    return 0;
}