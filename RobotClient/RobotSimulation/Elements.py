import pygame
from RobotSimulation.Util import Color

# Barrier setup
class Barrier:
    """
    Represents a barrier in the simulation.

    :ivar rect: The rectangle representing the barrier's position and size.
    :ivar color: The color of the barrier.
    """
    def __init__(self, x, y, width, height):
        """
        Initializes a Barrier object.

        :param x: The x-coordinate of the barrier.
        :param y: The y-coordinate of the barrier.
        :param width: The width of the barrier.
        :param height: The height of the barrier.
        """
        self.rect = pygame.Rect(x, y, width, height)
        self.color = Color.GREEN

    def draw(self, screen):
        """
        Draws the barrier on the screen.

        :param screen: The screen surface to draw the barrier on.
        """
        pygame.draw.rect(screen, self.color, self.rect)

# ColorPatch setup
class ColorPatch:
    """
    Represents a color patch in the simulation.

    :ivar rect: The rectangle representing the color patch's position and size.
    :ivar color: The color of the patch.
    """
    def __init__(self, x, y, width, height, color):
        """
        Initializes a ColorPatch object.

        :param x: The x-coordinate of the color patch.
        :param y: The y-coordinate of the color patch.
        :param width: The width of the color patch.
        :param height: The height of the color patch.
        :param color: The color of the patch.
        """
        self.rect = pygame.Rect(x, y, width, height)
        self.rect = pygame.Rect(x, y, width, height)
        self.color = color

    def draw(self, screen):
        """
        Draws the color patch on the screen.

        :param screen: The screen surface to draw the color patch on.
        """
        pygame.draw.rect(screen, self.color, self.rect)

# Button setup
class Button:
    """
    Represents a button in the simulation.

    :ivar text: The text displayed on the button.
    :ivar rect: The rectangle representing the button's position and size.
    :ivar callback: The function to call when the button is clicked.
    """
    def __init__(self, text, x, y, width, height, callback):
        """
        Initializes a Button object.

        :param text: The text displayed on the button.
        :param x: The x-coordinate of the button.
        :param y: The y-coordinate of the button.
        :param width: The width of the button.
        :param height: The height of the button.
        :param callback: The function to call when the button is clicked.
        """
        self.text = text
        self.rect = pygame.Rect(x, y, width, height)
        self.callback = callback

    def draw(self, screen):
        """
        Draws the button on the screen.

        :param screen: The screen surface to draw the button on.
        """
        pygame.draw.rect(screen, Color.WHITE, self.rect)
        font = pygame.font.Font(None, 36)
        text_surf = font.render(self.text, True, Color.BLACK)
        text_rect = text_surf.get_rect(center=self.rect.center)
        screen.blit(text_surf, text_rect)

    def handle_event(self, event):
        """
        Handles events related to the button.

        :param event: The event to handle.
        """
        if event.type == pygame.MOUSEBUTTONDOWN and self.rect.collidepoint(event.pos):
            self.callback()