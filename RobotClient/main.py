from rciapi import RobotClient
import RobotSimulation, json

# simulation = RobotSimulation.RobotSimulation()
# simulation.init()
# simulation.run()

robot_client = RobotClient(api_key="password")
response = robot_client.get_all_information()
print(json.dumps(response.get("light"), indent=4))
