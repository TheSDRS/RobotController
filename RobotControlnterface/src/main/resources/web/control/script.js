import {NodeGraph, Node} from '/control/nodeGraph.js';
import {DirectControlPanel} from '/control/directControls.js';

const canvasTop = () => {
    return document.getElementsByClassName('tabs')[0].getBoundingClientRect().bottom;
}

const canvasLeft = () => {
    return document.getElementById('toolbar').getBoundingClientRect().right;
}

// Initialize the node graph
const canvas = document.getElementById('canvas');
const graph = new NodeGraph(canvas);

// Drag-and-drop logic for nodes from the toolbar
document.querySelectorAll('.node-button').forEach(button => {
    button.addEventListener('mousedown', (event) => {
        const type = button.dataset.type;
        const name = button.dataset.name;
        const rect = canvas.getBoundingClientRect();

        const node = new Node(`${name}`, event.clientX - 60, event.clientY - 25, type);

        node.element.style.cursor = "grabbing";
        node.element.style.zIndex = "1000";
        node.element.style.boxShadow = "5px 5px 10px rgba(0, 0, 0, 1), 0 0 10px var(--tertiary-color), 0 0 10px var(--tertiary-color) inset";
        node.element.style.scale = "1.05";
        node.element.style.rotate = "1.5deg";

        const move = (e) => {
            node.x = e.clientX - 60;
            node.y = e.clientY - 25;
            node.updatePosition();
        };

        const stop = () => {
            document.removeEventListener('mousemove', move);
            document.removeEventListener('mouseup', stop);
            if (node.x >= rect.left && node.x <= rect.right && node.y >= rect.top && node.y <= rect.bottom) {
                graph.addNode(name, node.x, node.y, type);
            }
            node.destroy();
        };

        document.addEventListener('mousemove', move);
        document.addEventListener('mouseup', stop);
    });
});

// Bottom button event listeners
document.getElementById('startBtn').addEventListener('click', () => alert('Start clicked!'));
document.getElementById('stopBtn').addEventListener('click', () => alert('Stop clicked!'));
document.getElementById('pauseBtn').addEventListener('click', () => alert('Pause clicked!'));


// 3d UI for the direct control panel
const controlsContainer = document.querySelector('.direct-controls');
const directControlPanel = new DirectControlPanel(controlsContainer);

// Handle tabs
document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(tc => tc.classList.remove('active'));
        document.querySelectorAll('.node').forEach(n => n.classList.remove('active'));
        document.querySelectorAll('.svg-dot').forEach(n => n.classList.remove('active'));

        if (tab.dataset.tab == "nodes") {
            console.log("True");
            document.querySelectorAll('.node').forEach(n => n.classList.add('active'));
        }

        tab.classList.add('active');
        document.getElementById(`${tab.dataset.tab}-tab`).classList.add('active');

        // Trigger resize and update dot positions when switching to the "Direct" tab
        if (tab.dataset.tab == "direct") {
            document.querySelectorAll('.svg-dot').forEach(n => n.classList.add('active'));
            directControlPanel.loadInitialModels();
            directControlPanel.resizeRenderer();
            //directControlPanel.updateDotPositions();
        }
    });
});

function checkSessionValidity() {
    $.ajax({
        url: '/api',
        method: 'GET',
        xhrFields: {
            withCredentials: true
        },
        success: function(response, status, xhr) {
        },
        error: function(xhr, status, error) {
            window.location.href = '/control';
        }
    });
}

$(document).ready(() => {
    setInterval(checkSessionValidity, 10000);
})