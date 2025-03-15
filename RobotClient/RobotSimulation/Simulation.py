import pygame, sys
from RobotSimulation.Robot import Robot
from RobotSimulation.Util import Color
from RobotSimulation.Elements import Barrier, ColorPatch, Button

class Config:
    """
    Configuration class for the Robot Simulation.
    Stores various configuration parameters such as screen dimensions, robot scale, and sensor length.
    """
    def __init__(self):
        self.SCREEN_WIDTH = 1000
        self.SCREEN_HEIGHT = 800
        self.ROBOT_SCALE = 50
        self.SENSOR_LENGTH = 1000

    def get_screen_width(self):
        """
        Returns the screen width.
        """
        return self.SCREEN_WIDTH

    def get_screen_height(self):
        """
        Returns the screen height.
        """
        return self.SCREEN_HEIGHT

    def get_robot_scale(self):
        """
        Returns the robot scale.
        """
        return self.ROBOT_SCALE

    def get_sensor_length(self):
        """
        Returns the sensor length.
        """
        return self.SENSOR_LENGTH

    def set_screen_width(self, new_width):
        """
        Sets a new screen width.
        :param new_width: The new screen width.
        """
        self.SCREEN_WIDTH = new_width

    def set_screen_height(self, new_height):
        """
        Sets a new screen height.
        :param new_height: The new screen height.
        """
        self.SCREEN_HEIGHT = new_height

    def set_robot_scale(self, new_scale):
        """
        Sets a new robot scale.
        :param new_scale: The new robot scale.
        """
        self.ROBOT_SCALE = new_scale

    def set_sensor_length(self, new_length):
        """
        Sets a new sensor length.
        :param new_length: The new sensor length.
        """
        self.SENSOR_LENGTH = new_length

class RobotSimulation:
    """
    Main class for the Robot Simulation.
    Handles initialization, event processing, and the main simulation loop.
    """
    def __init__(self):
        self.config = Config()
        self.screen = None
        self.clock = None
        self.robot = None
        self.current_color = Color.YELLOW
        self.buttons = []
        self.barriers = []
        self.color_patches = []
        self.screen_edges = []

    def init(self):
        """
        Initializes the simulation, including Pygame, screen, clock, robot, barriers, and buttons.

        Important:
            This method should be called before run() to initialize the simulation.
        """
        print("Initializing Robot Simulation...")
        pygame.init()
        self.screen = pygame.display.set_mode((self.config.get_screen_width(), self.config.get_screen_height()))
        pygame.display.set_caption('Robot Simulation')
        self.clock = pygame.time.Clock()
        self.robot = Robot(self.config.SCREEN_WIDTH // 2, self.config.SCREEN_HEIGHT // 2, 0, Color.GREY, self.config.get_robot_scale(), self.config.get_sensor_length())
        self.barriers = [Barrier(self.config.get_screen_width() // 2 - 100, 200, 200, 50)]
        self.buttons = [
            Button('Left motor +', 50, self.get_config().get_screen_height() - 100, 150, 50, self.increase_left_motor_speed),
            Button('Left Motor -', 50, self.get_config().get_screen_height() - 40, 150, 50, self.decrease_left_motor_speed),
            Button('Both +', self.config.get_screen_width() // 2 - 75, self.get_config().get_screen_height() - 100, 150, 50, lambda: [self.increase_left_motor_speed(), self.increase_right_motor_speed()]),
            Button('Both -', self.config.get_screen_width() // 2 - 75, self.get_config().get_screen_height() - 40, 150, 50, lambda: [self.decrease_left_motor_speed(), self.decrease_right_motor_speed()]),
            Button('Right Motor +', self.config.get_screen_width() - 200, self.get_config().get_screen_height() - 100, 150, 50, self.increase_right_motor_speed),
            Button('Right Motor -', self.config.get_screen_width() - 200, self.get_config().get_screen_height() - 40, 150, 50, self.decrease_right_motor_speed),
            Button('Rotate Left', self.config.get_screen_width() - 200, 10, 150, 50, lambda: [self.increase_left_motor_speed(), self.decrease_right_motor_speed()]),
            Button('Rotate Right', self.config.get_screen_width() - 200, 70, 150, 50, lambda: [self.decrease_left_motor_speed(), self.increase_right_motor_speed()])
        ]
        screen_edges = [
            Barrier(0, 0, self.config.get_screen_width(), 1),  # Top edge
            Barrier(0, 0, 1, self.config.get_screen_height()),  # Left edge
            Barrier(0, self.config.get_screen_height() - 1, self.config.get_screen_width(), 1),  # Bottom edge
            Barrier(self.config.get_screen_width() - 1, 0, 1, self.config.get_screen_height())  # Right edge
        ]
        self.barriers.extend(screen_edges)
        print("Initialization complete!")

    def run(self):
        """
        Main loop of the simulation.
        Handles events, updates the robot, and draws all elements on the screen.

        Note:
            This method should be called after init() to start the simulation.
        """
        print("Running Robot Simulation...")
        while True:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()

                elif event.type == pygame.MOUSEBUTTONDOWN:
                    button_clicked = any(button.rect.collidepoint(event.pos) for button in self.buttons)
                    if not button_clicked:
                        if event.button == 3:
                            self.create_barrier(event.pos[0], event.pos[1])
                        elif event.button == 1:
                            self.create_color_patch(event.pos[0], event.pos[1])
                    for button in self.buttons:
                        button.handle_event(event)

            self.robot.update(self.barriers, self.color_patches, self.screen)

            self.screen.fill(Color.BLACK)
            self.robot.draw(self.screen)
            for barrier in self.barriers:
                barrier.draw(self.screen)
            for patch in self.color_patches:
                patch.draw(self.screen)
            for button in self.buttons:
                button.draw(self.screen)
            self.robot.display_info(self.screen)
            self.robot.draw_sensor_circle(self.screen)
            self.robot.get_ultrasonic_distance(self.barriers, self.screen)  # Draw lines on top

            # Draw pivot point for debugging
            if self.robot.pivot_point:
                pygame.draw.circle(self.screen, Color.YELLOW, (int(self.robot.pivot_point[0]), int(self.robot.pivot_point[1])), 5)

            pygame.display.flip()
            self.clock.tick(30)

    def increase_left_motor_speed(self):
        """Increases the speed of the left motor by 1."""
        self.robot.left_motor_speed += 1

    def decrease_left_motor_speed(self):
        """Decreases the speed of the left motor by 1."""
        self.robot.left_motor_speed -= 1

    def increase_right_motor_speed(self):
        """Increases the speed of the right motor by 1."""
        self.robot.right_motor_speed += 1

    def decrease_right_motor_speed(self):
        """Decreases the speed of the right motor by 1."""
        self.robot.right_motor_speed -= 1

    def set_left_motor_speed(self, speed):
        """Sets the speed of the left motor to the specified value."""
        self.robot.left_motor_speed = speed

    def set_right_motor_speed(self, speed):
        """Sets the speed of the right motor to the specified value."""
        self.robot.right_motor_speed = speed

    def create_color_patch(self, x, y):
        """
        Creates a color patch at the specified (x, y) coordinates.
        :param x: The x-coordinate of the color patch.
        :param y: The y-coordinate of the color patch.
        """
        self.color_patches.append(ColorPatch(x, y, 50, 50, self.current_color))

    def create_barrier(self, x, y):
        """
        Creates a barrier at the specified (x, y) coordinates.
        :param x: The x-coordinate of the barrier.
        :param y: The y-coordinate of the barrier.
        """
        self.barriers.append(Barrier(x, y, 100, 20))

    def get_config(self):
        """Returns the current configuration object."""
        return self.config