package wumpus_world;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wumpus_world_decisions.AgentPosition;
import wumpus_world_decisions.WumpusPercept;
import wumpus_world_decisions.WumpusAction;
import wumpus_world_decisions.EfficientHybridWumpusAgent;


public class NavigatorAgent extends Agent {
	EfficientHybridWumpusAgent agent;

    @Override
    protected void setup() {
        System.out.println("Hello! The navigator agent " + getAID().getName() + " is ready.");

        agent = new EfficientHybridWumpusAgent(4, 4, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_NORTH));
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(WumpusWorldAgent.Constants.NAVIGATOR_AGENT_TYPE);
        sd.setName(WumpusWorldAgent.Constants.NAVIGATOR_SERVICE_DESCRIPTION);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new LocationRequestsServer());
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("The navigator agent " + getAID().getName() + " terminating.");
    }

    private class LocationRequestsServer extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                if (parseSpeleologistMessageRequest(msg.getContent())){
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REQUEST);
                    reply.setContent(WumpusWorldAgent.Constants.INFORMATION_PROPOSAL_NAVIGATOR);
                    System.out.println("NavigatorAgent: " + WumpusWorldAgent.Constants.INFORMATION_PROPOSAL_NAVIGATOR);
                    myAgent.send(reply);
                } else if (parseSpeleologistMessageProposal(msg.getContent()))
                {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.PROPOSE);
                    String advice = getAdvice(msg.getContent());
                    reply.setContent(advice);
                    System.out.println("NavigatorAgent: " + advice);
                    myAgent.send(reply);

                } else
                    System.out.println("NavigatorAgent: Wrong message!");
            } else {
                block();
            }
        }

        private boolean parseSpeleologistMessageRequest(String instruction) {
            String regex = "\\bHelp\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(instruction);
            if (matcher.find()) {
                String res = matcher.group();
                return res.length() > 0;
            }
            return false;
        }

        private boolean parseSpeleologistMessageProposal(String instruction) {
            String regex = "\\bPosition\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(instruction);
            if (matcher.find()) {
                String res = matcher.group();
                return res.length() > 0;
            }
            return false;
        }

        private String getAdvice(String content){
        	WumpusPercept percept = new WumpusPercept();

            String advicedAction = "";

            for (Map.Entry<Integer, String> entry : STATES.entrySet()) {
                String value = entry.getValue();
                Pattern pattern = Pattern.compile("\\b" + value + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    switch (value){
                        case "Stench": percept.setStench();
                        case "Breeze": percept.setBreeze();
                        case "Glitter": percept.setGlitter();
                        case "Scream": percept.setScream();
                        case "Bump": percept.setBump();
                    }
                }
            }
            
            WumpusAction action = agent.act(percept).orElseThrow();
            
            String symbol = action.getSymbol();

	        switch (symbol) {
	        	case "Forward":
	        		advicedAction = WumpusWorldAgent.Constants.MESSAGE_FORWARD;
	            	break;
		        case "TurnLeft":
		        	advicedAction = WumpusWorldAgent.Constants.MESSAGE_LEFT;
	                break;
	            case "TurnRight":
	            	advicedAction = WumpusWorldAgent.Constants.MESSAGE_RIGHT;
	                break;
	            case "Grab":
	            	advicedAction = WumpusWorldAgent.Constants.MESSAGE_GRAB;
	                break;
	            case "Shoot":
	            	advicedAction = WumpusWorldAgent.Constants.MESSAGE_SHOOT;
	                break;
	            case "Climb":
	            	advicedAction = WumpusWorldAgent.Constants.MESSAGE_CLIMB;
	                break;
	        }

            int rand = 1 + (int) (Math.random() * 3);
            switch (rand) {
                case 1: return WumpusWorldAgent.Constants.ACTION_PROPOSAL1 + advicedAction;
                case 2: return WumpusWorldAgent.Constants.ACTION_PROPOSAL2 + advicedAction;
                case 3: return WumpusWorldAgent.Constants.ACTION_PROPOSAL3 + advicedAction;
                default: return "";
            }
        }

        final Map<Integer, String> STATES = new LinkedHashMap<Integer, String>() {{
            put(1, "Stench");
            put(2, "Breeze");
            put(3, "Glitter");
            put(4, "Scream");
        }};

    }
}