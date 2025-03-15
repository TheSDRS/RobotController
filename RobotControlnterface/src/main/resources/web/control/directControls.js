export class DirectControlPanel {
    constructor(container) {
        this.container = container;
        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
        this.renderer = new THREE.WebGLRenderer({antialias: true});
        this.controls = new THREE.OrbitControls(this.camera, this.renderer.domElement);
        this.models = [];
        this.objectInfoElements = {};
        this.svgDots = {};
        this.overlayCanvas = document.createElement('canvas');
        this.overlayContext = this.overlayCanvas.getContext('2d');
        this.mouse = new THREE.Vector2();
        this.raycaster = new THREE.Raycaster();
        this.INTERSECTED = null;
        this.clickedPoint = null;
        this.selectedObject = null;

        this.init();
    }

    init() {
        this.setupScene();
        this.setupRenderer();
        this.setupControls();
        this.setupEventListeners();
        this.animate();
    }

    setupScene() {
        this.scene.background = new THREE.Color(0x3a3a3a);
        this.addGroundPlane();
        this.addAxisLines();
        this.addAxisLabels();
        this.addLights();
    }

    setupRenderer() {
        this.renderer.setSize(this.container.clientWidth, this.container.clientHeight);
        this.renderer.shadowMap.enabled = true;
        this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
        this.container.appendChild(this.renderer.domElement);

        this.overlayCanvas.id = 'overlayCanvas';
        this.overlayCanvas.width = this.container.clientWidth;
        this.overlayCanvas.height = this.container.clientHeight;
        this.overlayCanvas.style.pointerEvents = 'none';
        this.overlayCanvas.style.zIndex = '1';
        this.container.appendChild(this.overlayCanvas);
    }

    setupControls() {
        this.controls.enableDamping = true;
        this.controls.dampingFactor = 0.25;
        this.controls.screenSpacePanning = false;
        this.controls.minDistance = 1;
        this.controls.maxDistance = 500;
        this.controls.maxPolarAngle = Math.PI / 2;
        this.controls.mouseButtons = {
            LEFT: THREE.MOUSE.ROTATE,
            MIDDLE: THREE.MOUSE.ROTATE,
            RIGHT: THREE.MOUSE.PAN
        };
        this.controls.touches = {
            ONE: THREE.TOUCH.ROTATE,
            TWO: THREE.TOUCH.DOLLY_PAN
        };
        this.controls.enablePan = false;
        this.controls.enableZoom = true;
        this.camera.position.set(45, 45, 45);
        this.camera.lookAt(this.scene.position);
    }

    setupEventListeners() {
        window.addEventListener('resize', this.resizeRenderer.bind(this));
        this.container.addEventListener('mousemove', this.onMouseMove.bind(this), false);
        this.renderer.domElement.addEventListener('click', this.onClick.bind(this), false);
    }

    resizeRenderer() {
        this.camera.aspect = this.container.clientWidth / this.container.clientHeight;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(this.container.clientWidth, this.container.clientHeight);
        this.overlayCanvas.width = this.container.clientWidth;
        this.overlayCanvas.height = this.container.clientHeight;
    }

    onMouseMove(event) {
        const rect = this.container.getBoundingClientRect();
        this.mouse.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
        this.mouse.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;
    }

    onClick(event) {
        const rect = this.container.getBoundingClientRect();
        this.mouse.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
        this.mouse.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;
        this.raycaster.setFromCamera(this.mouse, this.camera);
        const intersects = this.raycaster.intersectObjects(this.models, true);

        if (intersects.length > 0) {
            const clickedModel = intersects[0].object;
            const box = new THREE.Box3().setFromObject(clickedModel);
            this.clickedPoint = box.getCenter(new THREE.Vector3());
            this.selectedObject = clickedModel;

            if (this.objectInfoElements[clickedModel.name]) {
                this.objectInfoElements[clickedModel.name].style.display = 'block';
            }

            if (!this.svgDots[clickedModel.name]) {
                const svgDot = this.createSvgDot();
                this.svgDots[clickedModel.name] = svgDot;
            }
        } else {
            this.clickedPoint = null;
        }
    }

    addGroundPlane() {
        const groundGeometry = new THREE.PlaneGeometry(10000, 10000);
        groundGeometry.rotateX(-Math.PI / 2);
        groundGeometry.translate(0, -0.1, 0);
        const groundMaterial = new THREE.MeshStandardMaterial({
            color: new THREE.Color(0x3a3a3a),
            side: THREE.DoubleSide
        });
        const ground = new THREE.Mesh(groundGeometry, groundMaterial);
        ground.receiveShadow = true;
        this.scene.add(ground);
    }

    addAxisLines() {
        const createAxis = (size) => {
            const vertices = [
                new THREE.Vector3(-size, 0, 0), new THREE.Vector3(size, 0, 0),
                new THREE.Vector3(0, -size, 0), new THREE.Vector3(0, size, 0),
                new THREE.Vector3(0, 0, -size), new THREE.Vector3(0, 0, size)
            ];
            const colors = [
                new THREE.Color(0xff0000), new THREE.Color(0xff0000),
                new THREE.Color(0x00ff00), new THREE.Color(0x00ff00),
                new THREE.Color(0x0000ff), new THREE.Color(0x0000ff)
            ];
            const geometry = new THREE.BufferGeometry().setFromPoints(vertices);
            geometry.setAttribute('color', new THREE.Float32BufferAttribute(colors.flatMap(c => c.toArray()), 3));
            const material = new THREE.LineBasicMaterial({vertexColors: true});
            return new THREE.LineSegments(geometry, material);
        };

        const axisLines = createAxis(500);
        this.scene.add(axisLines);
    }

    addAxisLabels() {
        const createLabel = (text, position) => {
            const canvas = document.createElement('canvas');
            const context = canvas.getContext('2d');
            context.font = 'Bold 24px Arial';
            context.fillStyle = 'rgba(255, 255, 255, 1.0)';
            context.fillText(text, 0, 24);

            const texture = new THREE.CanvasTexture(canvas);
            const spriteMaterial = new THREE.SpriteMaterial({map: texture});
            const sprite = new THREE.Sprite(spriteMaterial);
            sprite.scale.set(10, 5, 1);
            sprite.position.copy(position);
            return sprite;
        };

        const xLabel = createLabel('X', new THREE.Vector3(500, 0, 0));
        const yLabel = createLabel('Y', new THREE.Vector3(0, 500, 0));
        const zLabel = createLabel('Z', new THREE.Vector3(0, 0, 500));

        this.scene.add(xLabel);
        this.scene.add(yLabel);
        this.scene.add(zLabel);
    }

    addLights() {
        const addSpotLight = (x, z) => {
            const spotLight = new THREE.SpotLight(0xffffff);
            spotLight.position.set(x, 40, z);
            spotLight.angle = Math.PI / 4;
            spotLight.penumbra = 0.5;
            spotLight.castShadow = true;
            spotLight.intensity = 0.25;
            spotLight.shadow.mapSize.width = 2048;
            spotLight.shadow.mapSize.height = 2048;
            spotLight.shadow.camera.near = 0.5;
            spotLight.shadow.camera.far = 50;
            spotLight.shadow.bias = -0.0001;
            const targetObject = new THREE.Object3D();
            targetObject.position.set(0, 0, 0);
            this.scene.add(targetObject);
            spotLight.target = targetObject;
            this.scene.add(spotLight);
        };

        addSpotLight(0, 0);
        addSpotLight(40, 0);
        addSpotLight(-40, 0);
        addSpotLight(0, 40);
        addSpotLight(0, -40);

        const groundLight = new THREE.DirectionalLight(0xffffff, 0.9255);
        groundLight.position.set(0, 50, 0);
        const groundTarget = new THREE.Object3D();
        groundTarget.position.set(0, 0, 0);
        this.scene.add(groundTarget);
        groundLight.target = groundTarget;
        groundLight.castShadow = true;
        groundLight.shadow.mapSize.width = 2048;
        groundLight.shadow.mapSize.height = 2048;
        groundLight.shadow.camera.near = 0.5;
        groundLight.shadow.camera.far = 100;
        groundLight.shadow.bias = -0.0001;
        this.scene.add(groundLight);
    }

    createSvgDot() {
        const svgDot = document.createElement('div');
        svgDot.className = 'svg-dot';
        fetch('/control/dot.svg')
            .then(response => response.text())
            .then(svgText => {
                svgDot.innerHTML = svgText;
            });
        this.container.appendChild(svgDot);
        return svgDot;
    }

    loadInitialModels() {
        const modelPaths = ['/control/teapot.obj', '/control/teapot.obj'];
        const modelNames = ['TeaPot', 'TeaPot_02'];
        const modelPositions = [new THREE.Vector3(0, 0, 0), new THREE.Vector3(25, 0, 0)];

        const loadModelPromises = modelPaths.map((path, index) => {
            if (!this.models.some(model => model.name === modelNames[index])) {
                return this.loadModelFromPath(path).then((baseModel) => {
                    baseModel.position.copy(modelPositions[index]);
                    this.setupModel(baseModel, modelNames[index]);
                }).catch((error) => {
                    console.error(`An error happened while loading the model ${modelNames[index]}:`, error);
                });
            } else {
                return Promise.resolve();
            }
        });

        Promise.all(loadModelPromises).then(() => {
            console.log('All models loaded');
        });
    }

    loadModelFromPath(filePath) {
        return new Promise((resolve, reject) => {
            const loader = new THREE.FileLoader();
            loader.load(filePath, (data) => {
                const newLoader = new THREE.OBJLoader();
                const baseModel = newLoader.parse(data);
                resolve(baseModel);
            }, undefined, reject);
        });
    }

    setupModel(baseModel, name) {
        const box = new THREE.Box3().setFromObject(baseModel);
        baseModel.position.y = box.min.y * -1;
        baseModel.scale.set(100, 100, 100);
        baseModel.traverse((child) => {
            if (child.isMesh) {
                child.name = name;
                child.castShadow = true;
                child.receiveShadow = true;
                child.material.map = new THREE.TextureLoader().load('/control/teapot.png');
                child.material.needsUpdate = true;
            }
        });

        this.scene.add(baseModel);
        this.models.push(baseModel);
        baseModel.name = name;
        this.objectInfoElements[baseModel.name] = this.createObjectInfoElement(baseModel.name);
    }

    createObjectInfoElement(name) {
        const element = document.createElement('div');
        element.id = `${name}Info`;
        element.className = 'object-info';
        element.innerHTML = `
            <div class="title-container">
                <strong>${name}</strong>
            </div>
            <hr class="divider">
            Additional info here
        `;

        if (name === "TeaPot") {
            const button = document.createElement('button');
            button.textContent = 'Button for TeaPot';
            button.addEventListener('click', (event) => {
                event.stopPropagation();
                console.log('Button for TeaPot clicked');
            });
            element.appendChild(button);
        } else if (name === "TeaPot_02") {
            const slider = document.createElement('input');
            slider.type = 'range';
            slider.min = '1';
            slider.max = '100';
            slider.value = '50';
            slider.addEventListener('input', (event) => {
                event.stopPropagation();
                console.log('Slider value:', event.target.value);
            });
            element.appendChild(slider);
        }

        const closeButton = document.createElement('button');
        closeButton.className = 'close-btn';
        closeButton.innerHTML = '&times;';
        closeButton.addEventListener('click', (event) => {
            event.stopPropagation();
            element.style.display = 'none';
            if (this.svgDots[name]) {
                this.svgDots[name].remove();
                delete this.svgDots[name];
            }
        });
        element.appendChild(closeButton);

        let offsetX, offsetY;
        const titleContainer = element.querySelector('.title-container');
        titleContainer.addEventListener('mousedown', (event) => {
            offsetX = event.clientX - element.getBoundingClientRect().left;
            offsetY = event.clientY - element.getBoundingClientRect().top;
            element.style.opacity = '1';
            element.style.zIndex = '1005';
            document.addEventListener('mousemove', onMouseMove);
            document.addEventListener('mouseup', onMouseUp);
        });

        const onMouseMove = (event) => {
            const rect = this.container.getBoundingClientRect();
            let newLeft = event.clientX - offsetX;
            let newTop = event.clientY - offsetY;

            newLeft = Math.max(rect.left, Math.min(newLeft, rect.right - element.clientWidth));
            newTop = Math.max(rect.top, Math.min(newTop, rect.bottom - element.clientHeight));

            element.style.left = `${newLeft - rect.left}px`;
            element.style.top = `${newTop - rect.top}px`;
        };

        const onMouseUp = () => {
            element.style.opacity = '1';
            element.style.zIndex = '1000';
            document.removeEventListener('mousemove', onMouseMove);
            document.removeEventListener('mouseup', onMouseUp);
        };

        this.container.appendChild(element);
        return element;
    }

    animate() {
        requestAnimationFrame(this.animate.bind(this));

        this.raycaster.setFromCamera(this.mouse, this.camera);
        const intersects = this.raycaster.intersectObjects(this.models, true);


        if (intersects.length > 0) {
            if (this.INTERSECTED !== intersects[0].object) {
                if (this.INTERSECTED) {
                    this.INTERSECTED.material.emissive.setHex(this.INTERSECTED.currentHex);
                }
                this.INTERSECTED = intersects[0].object;
                this.INTERSECTED.currentHex = this.INTERSECTED.material.emissive.getHex();
                this.INTERSECTED.material.emissive.setHex(0x806800);
            }
        } else {
            if (this.INTERSECTED) {
                this.INTERSECTED.material.emissive.setHex(this.INTERSECTED.currentHex);
            }
            this.INTERSECTED = null;
        }

        this.overlayContext.clearRect(0, 0, this.overlayCanvas.width, this.overlayCanvas.height);
        Object.keys(this.objectInfoElements).forEach((name) => {
            const objectInfoElement = this.objectInfoElements[name];
            if (objectInfoElement.style.display === 'block') {
                const model = this.models.find(m => m.name === name);
                if (model) {
                    const box = new THREE.Box3().setFromObject(model);
                    const center = box.getCenter(new THREE.Vector3());
                    const vector = center.clone().project(this.camera);
                    const x = (vector.x * 0.5 + 0.5) * this.overlayCanvas.clientWidth;
                    const y = (vector.y * -0.5 + 0.5) * this.overlayCanvas.clientHeight;

                    const svgDot = this.svgDots[name];
                    if (svgDot) {
                        svgDot.style.left = `${x - svgDot.offsetWidth / 2}px`;
                        svgDot.style.top = `${y - svgDot.offsetHeight / 2}px`;
                    }

                    const rect = objectInfoElement.getBoundingClientRect();
                    const elementX = rect.left + rect.width / 2;
                    const elementY = rect.top + rect.height / 2 - document.querySelector('.tabs').getBoundingClientRect().bottom;

                    this.overlayContext.strokeStyle = getComputedStyle(document.documentElement).getPropertyValue('--line-color');
                    this.overlayContext.lineWidth = parseFloat(getComputedStyle(document.documentElement).getPropertyValue('--line-width'));
                    this.overlayContext.beginPath();
                    this.overlayContext.moveTo(x, y);
                    this.overlayContext.lineTo(elementX, elementY);
                    this.overlayContext.stroke();
                }
            }
        });

        this.controls.update();
        this.renderer.render(this.scene, this.camera);
        this.updateCameraInfo();
    }

    updateCameraInfo() {
        const cameraInfo = document.getElementById('cameraInfo');
        cameraInfo.innerHTML = `
            Position: (${this.camera.position.x.toFixed(2)}, ${this.camera.position.y.toFixed(2)}, ${this.camera.position.z.toFixed(2)})<br>
            Rotation: (${this.camera.rotation.x.toFixed(2)}, ${this.camera.rotation.y.toFixed(2)}, ${this.camera.rotation.z.toFixed(2)})
        `;
    }
}