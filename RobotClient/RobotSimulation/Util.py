import pygame, math

class Color:
    WHITE = (255, 255, 255)
    BLACK = (0, 0, 0)
    GREY = (169, 169, 169)
    RED = (255, 0, 0)
    GREEN = (0, 255, 0)
    BLUE = (0, 0, 255)
    YELLOW = (255, 255, 0)

def line_intersection(p1, p2, p3, p4):
    """
    Calculates the intersection point of two lines.

    :param p1: The start point of the first line.
    :type p1: tuple
    :param p2: The end point of the first line.
    :type p2: tuple
    :param p3: The start point of the second line.
    :type p3: tuple
    :param p4: The end point of the second line.
    :type p4: tuple
    :returns: The intersection point, or None if the lines do not intersect.
    :rtype: tuple
    """
    s1_x = p2[0] - p1[0]
    s1_y = p2[1] - p1[1]
    s2_x = p4[0] - p3[0]
    s2_y = p4[1] - p3[1]

    denominator = (-s2_x * s1_y + s1_x * s2_y)
    if denominator == 0:
        return None  # Lines are parallel

    s = (-s1_y * (p1[0] - p3[0]) + s1_x * (p1[1] - p3[1])) / denominator
    t = (s2_x * (p1[1] - p3[1]) - s2_y * (p1[0] - p3[0])) / denominator

    if 0 <= s <= 1 and 0 <= t <= 1:
        # Intersection detected
        intersection_x = p1[0] + (t * s1_x)
        intersection_y = p1[1] + (t * s1_y)
        return intersection_x, intersection_y

    return None

def calculate_distance_to_barrier(start, end, rect, screen):
    """
    Calculates the distance to a barrier.

    :param start: The start point of the line.
    :type start: tuple
    :param end: The end point of the line.
    :type end: tuple
    :param rect: The rectangle representing the barrier.
    :type rect: pygame.Rect
    :param screen: The screen surface to draw on.
    :type screen: pygame.Surface
    :returns: The distance to the barrier.
    :rtype: float
    """
    # Define the four edges of the rectangle
    edges = [
        ((rect.left, rect.top), (rect.right, rect.top)),
        ((rect.right, rect.top), (rect.right, rect.bottom)),
        ((rect.right, rect.bottom), (rect.left, rect.bottom)),
        ((rect.left, rect.bottom), (rect.left, rect.top))
    ]

    # Draw the edges as lines
    for edge_start, edge_end in edges:
        pygame.draw.line(screen, Color.BLUE, edge_start, edge_end, 2)

    # Check for intersection with each edge
    min_distance = None
    for edge_start, edge_end in edges:
        intersection = line_intersection(start, end, edge_start, edge_end)
        if intersection:
            distance = math.hypot(intersection[0] - start[0], intersection[1] - start[1])
            if min_distance is None or distance < min_distance:
                min_distance = distance
    return min_distance