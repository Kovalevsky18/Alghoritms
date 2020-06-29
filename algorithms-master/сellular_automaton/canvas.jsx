const canvas = document.querySelector('#game');
const ctx = canvas.getContext('2d');

const Game = new LifeGame(ROWS_NUMBER, COLUMNS_NUMBER)

const resetCanvas = (canvas, ctx) => {
    ctx.fillStyle = BACKGROUND_COLOR;

    ctx.beginPath();
    ctx.rect(0, 0, canvas.width, canvas.height);

    ctx.fill();
}

const drawField = (x, y, color) => {
    ctx.fillStyle = color;

    ctx.beginPath();
    ctx.rect(x * FIELD_SIZE, y * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE);

    ctx.fill();
}

const tick = timestamp => {
    resetCanvas(canvas, ctx);

    if (timestamp > Game.generationNumber * GENERATION_TIME) {
        Game.changeGeneration()
    }

    Game.applyCallback((x, y) => drawField(x, y, FIELD_COLOR))

    requestAnimationFrame(tick);
}

const initGameCanvas = (canvas, Game) => {
    canvas.width = FIELD_SIZE * COLUMNS_NUMBER;
    canvas.height = FIELD_SIZE * ROWS_NUMBER;

    Game.reviveRandomFields(ROWS_NUMBER * COLUMNS_NUMBER / 2);

    requestAnimationFrame(tick);
}

initGameCanvas(canvas, Game);