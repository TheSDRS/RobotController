const canvasTop = () => {
    return document.getElementsByClassName('tabs')[0].getBoundingClientRect().bottom;
}

const canvasLeft = () => {
    return document.getElementById('toolbar').getBoundingClientRect().right;
}

export class NodeGraph {
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');
        this.nodes = [];
        this.connections = [];
        this.currentConnection = null; // Active connection being created
        this.connectionToRemove = null;

        this.resizeCanvas();
        window.addEventListener('resize', () => this.resizeCanvas());

        // Mouse event listeners for the canvas
        this.canvas.addEventListener('mousemove', (e) => this.handleMouseMove(e));
        this.canvas.addEventListener('mouseup', (e) => this.handleMouseUp(e));
    }

    resizeCanvas() {
        this.canvas.width = window.innerWidth - canvasLeft(); // Adjust for toolbar width
        this.canvas.height = window.innerHeight - canvasTop();
        this.canvas.style.left = canvasLeft();
        this.canvas.style.top = canvasTop();
        this.draw();
    }

    addNode(name ,x, y, type) {
        const id = name;
        const node = new Node(id, x, y, type);
        this.nodes.push(node);

        // Add event listeners for node and connection points
        node.element.addEventListener('mousedown', (e) => this.handleNodeMouseDown(node, e));
        node.element.addEventListener('mousemove', (e) => this.handleMouseMove(e));
        if (type === "N") {
            node.inputPoint.addEventListener('mousedown', (e) => this.startConnection(node, 'input', e));
            node.inputPoint.addEventListener('dblclick', (e) => this.deleteConnection(e));
            node.outputPoint.addEventListener('mousedown', (e) => this.startConnection(node, 'output', e));
            node.outputPoint.addEventListener('dblclick', (e) => this.deleteConnection(node, e));
        } else if (type === "START") {
            node.outputPoint.addEventListener('mousedown', (e) => this.startConnection(node, 'output', e));
            node.outputPoint.addEventListener('dblclick', (e) => this.deleteConnection(e));
        } else if (type === "END") {
            node.inputPoint.addEventListener('mousedown', (e) => this.startConnection(node, 'input', e));
            node.inputPoint.addEventListener('dblclick', (e) => this.deleteConnection(e));
        }

        document.body.appendChild(node.element);
        this.draw();
    }

    handleNodeMouseDown(node, event) {
        if (event.target === node.inputPoint || event.target === node.outputPoint) {
            console.log("Clicked on a connection point.");
            this.draw();
            return;
        }

        // Dragging the node
        const offsetX = event.clientX - node.x;
        const offsetY = event.clientY - node.y;
        const rect = canvas.getBoundingClientRect();

        const onMouseMove = (e) => {
            node.x = e.clientX - offsetX;
            node.y = e.clientY - offsetY;
            node.updatePosition();
            this.draw();
        };

        const onMouseUp = (e) => {
            if (e.clientX <= rect.left || node.y <= rect.top) {
                this.nodes.splice(graph.nodes.indexOf(node), 1);
                node.destroy();
                this.draw();
            }
            document.removeEventListener('mousemove', onMouseMove);
            document.removeEventListener('mouseup', onMouseUp);
        };

        document.addEventListener('mousemove', onMouseMove);
        document.addEventListener('mouseup', onMouseUp);
    }

    handleMouseMove(event) {
        this.draw();
    }

    handleMouseUp(event) {
        this.draw();
    }

    startConnection(node, type, event) {
        event.stopPropagation();
        const startPoint = node.getCenterPoint()[type];
        this.currentConnection = { from: node, to: null, startPoint, endPoint: { x: event.clientX, y: event.clientY } };

        const onMouseMove = (e) => {
            this.currentConnection.endPoint = { x: e.clientX, y: e.clientY };
            this.draw();
        };

        const onMouseUp = (e) => {
            this.completeConnection(e);
            document.removeEventListener('mousemove', onMouseMove);
            document.removeEventListener('mouseup', onMouseUp);
            this.draw();
        };

        document.addEventListener('mousemove', onMouseMove);
        document.addEventListener('mouseup', onMouseUp);
    }

    completeConnection(event) {
        const { x: mouseX, y: mouseY } = event;

        // If no connection is in progress, log and exit
        if (!this.currentConnection || !this.currentConnection.startPoint) {
            console.error("No valid connection in progress.");
            return;
        }

        // If released on canvas, discard the connection
        if (event.target === this.canvas) {
            console.warn("Connection discarded: released on canvas.");
            this.currentConnection = null;
            this.draw();
            return;
        }

        // Iterate over nodes to find a valid input point
        for (const node of this.nodes) {
            const centerPoints = node.getCenterPoint();

            // Skip `START` nodes when looking for input points
            if (node.type === "START") {
                console.warn(`Node ${node.id} is missing an input point (ignored).`);
                continue;
            }

            // Skip `END` nodes when looking for output points
            if (node.type === "END" && !centerPoints.input) {
                console.warn(`Node ${node.id} is missing an input point.`);
                continue;
            }

            // Check proximity to an input point
            const inputPoint = centerPoints.input;
            if (inputPoint && Math.hypot(inputPoint.x - mouseX, inputPoint.y - mouseY) < 10) {
                // Prevent self-connection
                if (this.currentConnection.from === node) {
                    console.warn("Cannot connect a node to itself.");
                    this.currentConnection = null;
                    this.draw();
                    return;
                }

                // Add the connection
                this.connections.push({
                    from: this.currentConnection.from,
                    to: node,
                });

                this.currentConnection = null; // Clear the current connection
                this.draw();
                return;
            }
        }

        console.warn("No valid input point found. Connection discarded.");
        this.currentConnection = null;
        this.draw();
    }

    deleteConnection(event) {
        for (const connection of this.connections) {
            const from = connection.from.getCenterPoint().output;
            const to = connection.to.getCenterPoint().input;
            const x = event.target.getBoundingClientRect().left + event.target.offsetWidth / 2;
            const y = event.target.getBoundingClientRect().top + event.target.offsetHeight / 2;
            if (event.target.classList.contains('input-point') && Math.hypot(x - to.x, y - to.y) < 10 ) {
                this.connectionToRemove = this.connections.indexOf(connection);
            }
            if (event.target.classList.contains('output-point') && Math.hypot(x - from.x, y - from.y) < 10) {
                this.connectionToRemove = this.connections.indexOf(connection);
            }
        }

        if (this.connectionToRemove != null) {
            this.connections.splice(this.connectionToRemove, 1);
            this.connectionToRemove = null;
        }

        this.draw();
    }


    draw() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        // Draw all connections
        for (const connection of this.connections) {
            const from = connection.from.getCenterPoint().output;
            const to = connection.to.getCenterPoint().input;

            if (!this.nodes.includes(connection.from) || !this.nodes.includes(connection.to)) {
                this.connectionToRemove = this.connections.indexOf(connection);
            } else {
                this.drawBezierCurve(from, to);
            }
        }

        if (this.connectionToRemove != null) {
            this.connections.splice(this.connectionToRemove, 1);
            this.connectionToRemove = null;
        }

        // Draw the current connection being created
        if (this.currentConnection) {
            const { startPoint, endPoint } = this.currentConnection;
            this.drawBezierCurve(startPoint, endPoint, true);
        }
    }

    drawBezierCurve(from, to, isTemporary = false) {
        const fromX = from.x - canvasLeft();
        const fromY = from.y - canvasTop();
        const toX = to.x - canvasLeft();
        const toY  = to.y - canvasTop();
        const cp1X = fromX + 100;
        const cp1Y = fromY;
        const cp2X = toX - 100;
        const cp2Y = toY;

        this.ctx.beginPath();
        this.ctx.moveTo(fromX, fromY);
        this.ctx.bezierCurveTo(cp1X, cp1Y, cp2X, cp2Y, toX, toY);
        this.ctx.strokeStyle = isTemporary ? '#e9be00' : '#bda125';
        this.ctx.lineWidth = 2;
        this.ctx.stroke();
    }
}

export class Node {
    constructor(id, x, y, type) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;

        // Create the node element
        this.element = document.createElement('div');
        this.element.classList.add('node');
        this.element.classList.add('active');
        this.element.style.left = `${x}px`;
        this.element.style.top = `${y}px`;
        this.element.innerHTML = `<p>${id}</p>`;
        this.element.style.userSelect = 'none';

        if (type == "N") {
            // Create input and output points
            this.inputPoint = document.createElement('div');
            this.inputPoint.className = 'input-point';
            this.element.appendChild(this.inputPoint);

            this.outputPoint = document.createElement('div');
            this.outputPoint.className = 'output-point';
            this.element.appendChild(this.outputPoint);
        }

        if (type == "START") {
            this.outputPoint = document.createElement('div');
            this.outputPoint.className = 'output-point';
            this.element.appendChild(this.outputPoint);
            this.element.classList.add('startNode');
        }

        if (type == "END") {
            this.inputPoint = document.createElement('div');
            this.inputPoint.className = 'input-point';
            this.element.appendChild(this.inputPoint);
            this.element.classList.add('endNode');
        }

        document.body.appendChild(this.element);
    }

    updatePosition() {
        this.element.style.left = `${this.x}px`;
        this.element.style.top = `${this.y}px`;
    }

    getCenterPoint() {
        const rect = this.element.getBoundingClientRect();

        if (this.type === "N") {
            // Ensure input and output points exist for "N" type nodes
            if (!this.inputPoint || !this.outputPoint) {
                console.error(`Node ${this.id} is missing input or output points.`);
                return {};
            }
            return {
                input: {
                    x: rect.left + this.inputPoint.offsetWidth / 2 - 5,
                    y: rect.top + this.inputPoint.offsetHeight / 2 + 20,
                },
                output: {
                    x: rect.right - this.outputPoint.offsetWidth / 2 + 5,
                    y: rect.top + this.outputPoint.offsetHeight / 2 + 20,
                },
            };
        }

        if (this.type === "START") {
            // Ensure output point exists for "START" nodes
            if (!this.outputPoint) {
                console.error(`Start Node ${this.id} is missing an output point.`);
                return {};
            }
            return {
                output: {
                    x: rect.right - this.outputPoint.offsetWidth / 2 + 5,
                    y: rect.top + this.outputPoint.offsetHeight / 2 + 20,
                },
            };
        }

        if (this.type === "END") {
            // Ensure input point exists for "END" nodes
            if (!this.inputPoint) {
                console.error(`End Node ${this.id} is missing an input point.`);
                return {};
            }
            return {
                input: {
                    x: rect.left + this.inputPoint.offsetWidth / 2 - 5,
                    y: rect.top + this.inputPoint.offsetHeight / 2 + 20,
                },
            };
        }

        console.error(`Unknown node type for Node ${this.id}.`);
        return {};
    }

    destroy() {
        document.body.removeChild(this.element);
        this.element.remove();
    }
}