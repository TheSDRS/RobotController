import pygame
from RobotSimulation.Util import *
from RobotSimulation.Util import Color

# Robot setup
class Robot:
    """
    Represents a robot in the simulation.

    :ivar x: The x-coordinate of the robot.
    :ivar y: The y-coordinate of the robot.
    :ivar angle: The angle of the robot.
    :ivar color: The color of the robot.
    :ivar scale: The scale of the robot.
    :ivar left_motor_speed: The speed of the left motor.
    :ivar right_motor_speed: The speed of the right motor.
    :ivar ultrasonic_distance: The distance measured by the ultrasonic sensor.
    :ivar max_sensor_distance: The maximum distance the ultrasonic sensor can measure.
    :ivar color_sensor_value: The value of the color sensor.
    :ivar pivot_point: The pivot point coordinates.
    """
    def __init__(self, x, y, angle: int, color: Color, scale: int, max_sensor_distance: int):
        """
        Initializes a Robot object.

        :param x: The x-coordinate of the robot.
        :param y: The y-coordinate of the robot.
        :param angle: The angle of the robot.
        :param color: The color of the robot.
        :param scale: The scale of the robot.
        :param max_sensor_distance: The maximum distance the ultrasonic sensor can measure.
        """
        print("Initializing Robot...")
        self.x = x
        self.y = y
        self.angle = angle
        self.color = color
        self.scale = scale
        self.left_motor_speed = 0
        self.right_motor_speed = 0
        self.ultrasonic_distance = max_sensor_distance
        self.max_sensor_distance = max_sensor_distance
        self.color_sensor_value = Color.WHITE
        self.pivot_point = None  # Store pivot point coordinates

    def get_sensor_start_point(self) -> tuple:
        """
        Calculates the start point from the edge of the robot.
        :return: a tuple representing the start point coordinates.
        """
        start_point = (
            self.x + (self.scale // 2) * pygame.math.Vector2(1, 0).rotate(-self.angle).x,
            self.y + (self.scale // 2) * pygame.math.Vector2(1, 0).rotate(-self.angle).y
        )
        return start_point

    def get_sensor_end_point(self) -> tuple:
        """
        Calculates the end point of the ultrasonic sensor line.
        :return: a tuple representing the end point coordinates.
        """
        start_point = self.get_sensor_start_point()
        end_point = (
            start_point[0] + self.ultrasonic_distance * pygame.math.Vector2(1, 0).rotate(-self.angle).x,
            start_point[1] + self.ultrasonic_distance * pygame.math.Vector2(1, 0).rotate(-self.angle).y
        )
        return end_point

    def draw(self, screen):
        """
        Draws the robot on the screen.

        :param screen: The screen surface to draw the robot on.
        """
        # Create a new surface for the robot
        robot_surface = pygame.Surface((self.scale, self.scale))
        robot_surface.fill(self.color)
        robot_surface.set_colorkey(Color.BLACK)  # Set a colorkey for transparency

        # Rotate the surface
        rotated_surface = pygame.transform.rotate(robot_surface, self.angle)
        rotated_rect = rotated_surface.get_rect(center=(self.x, self.y))

        # Draw the rotated surface
        screen.blit(rotated_surface, rotated_rect.topleft)

        # Calculate the start point from the edge of the robot
        start_point = self.get_sensor_start_point()
        end_point = self.get_sensor_end_point()

        # Draw ultrasonic sensor line on top
        pygame.draw.line(screen, Color.RED, start_point, (end_point[0], end_point[1]), 2)

    def draw_sensor_circle(self, screen):
        """
        Draws a circle at the end point of the ultrasonic sensor line.

        :param screen: The screen surface to draw the circle on.
        """
        # Draw a circle at the end point of the ultrasonic sensor line
        end_point = self.get_sensor_end_point()
        pygame.draw.circle(screen, Color.RED, (int(end_point[0]), int(end_point[1])), 5)

    def update(self, barriers, color_patches, screen):
        """
        Updates the robot's position, angle, and sensor values.

        :param barriers: A list of Barrier objects.
        :param color_patches: A list of ColorPatch objects.
        :param screen: The screen surface to draw on.
        """
        if self.left_motor_speed == 0 and self.right_motor_speed != 0:
            # Rotate around the center of the left edge
            pivot_x, pivot_y = self.set_pivot()
            self.angle += self.right_motor_speed
            self.pivot_point = (pivot_x, pivot_y)
        elif self.right_motor_speed == 0 and self.left_motor_speed != 0:
            # Rotate around the center of the right edge
            pivot_x, pivot_y = self.set_pivot()
            self.angle -= self.left_motor_speed
            self.pivot_point = (pivot_x, pivot_y)
        else:
            # Rotate around the center
            new_x = self.x + self.left_motor_speed * pygame.math.Vector2(1, 0).rotate(-self.angle).x
            new_y = self.y + self.right_motor_speed * pygame.math.Vector2(1, 0).rotate(-self.angle).y
            new_angle = self.angle + (self.right_motor_speed - self.left_motor_speed)

            # Create a new surface for the robot
            robot_surface = pygame.Surface((self.scale, self.scale))
            robot_surface.fill(self.color)
            robot_surface.set_colorkey(Color.BLACK)  # Set a colorkey for transparency

            # Rotate the surface
            rotated_surface = pygame.transform.rotate(robot_surface, new_angle)
            rotated_rect = rotated_surface.get_rect(center=(new_x, new_y))

            # Check for collisions with barriers
            collision = any(rotated_rect.colliderect(barrier.rect) for barrier in barriers)
            if not collision:
                self.x = new_x
                self.y = new_y
                self.angle = new_angle
            self.pivot_point = (self.x, self.y)  # Center pivot point

        # Update ultrasonic sensor distance
        self.ultrasonic_distance = self.get_ultrasonic_distance(barriers, screen)

        # Update color sensor value
        self.color_sensor_value = self.get_color_sensor_value(color_patches)

    def set_pivot(self):
        """
        Sets the pivot point for rotation.

        :returns: The pivot point coordinates.
        :rtype: tuple
        """
        pivot_x = self.x - (self.scale // 2) * pygame.math.Vector2(0, 1).rotate(-self.angle).x
        pivot_y = self.y - (self.scale // 2) * pygame.math.Vector2(0, 1).rotate(-self.angle).y
        self.x = pivot_x + (self.scale // 2) * pygame.math.Vector2(0, 1).rotate(-self.angle).x
        self.y = pivot_y + (self.scale // 2) * pygame.math.Vector2(0, 1).rotate(-self.angle).y
        return pivot_x, pivot_y

    def get_ultrasonic_distance(self, barriers, screen):
        """
        Gets the distance to the nearest barrier using the ultrasonic sensor.

        :param barriers: A list of Barrier objects.
        :type barriers: list
        :param screen: The screen surface to draw on.
        :type screen: pygame.Surface
        :returns: The distance to the nearest barrier.
        :rtype: float
        """
        # Calculate the start point from the edge of the robot
        start_point = self.get_sensor_start_point()
        end_point = self.get_sensor_end_point()
        min_distance = self.max_sensor_distance
        for barrier in barriers:
            distance = calculate_distance_to_barrier(start_point, end_point, barrier.rect, screen)
            if distance is not None:
                min_distance = min(min_distance, distance)

        return min_distance

    def get_color_sensor_value(self, color_patches):
        """
        Gets the value of the color sensor.

        :param color_patches: A list of ColorPatch objects.
        :type color_patches: list
        :returns: The color detected by the sensor.
        :rtype: Color
        """
        sensor_rect = pygame.Rect(self.x + self.scale // 2 - self.max_sensor_distance // 2,
                                  self.y + self.scale - self.max_sensor_distance // 2,
                                  self.max_sensor_distance, self.max_sensor_distance)
        for patch in color_patches:
            if sensor_rect.colliderect(patch.rect):
                return patch.color
        return Color.WHITE

    def display_info(self, screen):
        """
        Displays the robot's information on the screen.

        :param screen: The screen surface to draw the information on.
        :type screen: pygame.Surface
        """
        font = pygame.font.Font(None, 36)
        distance_text = font.render(f'Distance: {self.ultrasonic_distance:.2f} cm', True, Color.WHITE)
        color_text = font.render(f'Color: {self.color_sensor_value}', True, Color.WHITE)
        left_motor_text = font.render(f'Left Motor Speed: {self.left_motor_speed}', True, Color.WHITE)
        right_motor_text = font.render(f'Right Motor Speed: {self.right_motor_speed}', True, Color.WHITE)

        screen.blit(distance_text, (10, 10))
        screen.blit(color_text, (10, 50))
        screen.blit(left_motor_text, (10, 90))
        screen.blit(right_motor_text, (10, 130))