const fill2DArray = (rows, columns) => {
    const map = [];

    for (let y = 0; y < rows; y++) {
        const row = [];
        for (let x = 0; x < columns; x++)
            row.push(false);

        map.push(row);
    }
    return map;
}

class LifeGame {
    constructor(rows, columns) {
        this.rows = rows;
        this.columns = columns;
        this.generationNumber = 0;

        this.drawMap = fill2DArray(this.rows, this.columns);
    }

    countNeighborsNumber(x, y) {
        let neighborCount = 0;

        for (let dx = -1; dx <= 1; dx++) {
            for (let dy = -1; dy <= 1; dy++) {
                if (dx === 0 && dy === 0)
                    continue;

                neighborCount += this.getField(x + dx, y + dy)
            }
        }

        return neighborCount;
    }

    changeGeneration() {
        const drawMap = [];

        for (let y = 0; y < this.rows; y++) {
            const row = []

            for (let x = 0; x < this.columns; x++) {
                const neighborCount = this.countNeighborsNumber(x, y);
                let state = false;

                if (this.getField(x, y)) {
                    if (RESSURECTION_FOR_ALIVE.includes(neighborCount))
                        state = true;
                } else
                    if (RESSURECTION_FOR_DEAD.includes(neighborCount))
                        state = true;

                row.push(state);
            }

            drawMap.push(row);
        }

        this.drawMap = drawMap;
        this.generationNumber++;
    }

    reviveRandomFields(numberOfFields = 1) {
        const freeFields = []

        for (let y = 0; y < this.rows; y++)
            for (let x = 0; x < this.columns; x++)
                if (!this.getField(x, y))
                    freeFields.push({ x, y });

        numberOfFields = Math.min(numberOfFields, freeFields.length);

        while (numberOfFields-- > 0) {
            const indexOfFreeField = Math.floor(Math.random() * freeFields.length)
            const { x, y } = freeFields.splice(indexOfFreeField, 1)[0]
            this.setField(x, y, true)
        }
    }

    applyCallback(callback) {
        for (let y = 0; y < this.rows; y++)
            for (let x = 0; x < this.columns; x++)
                if (this.getField(x, y))
                    callback(x, y);
    }

    isCoordinateOutsideTheBorder(x, y) {
        return x < 0 || x >= this.columns || y < 0 || y >= this.rows
    }

    getField(x, y) {
        if (this.isCoordinateOutsideTheBorder(x, y))
            return false;

        return this.drawMap[y][x];
    }

    setField(x, y, value) {
        if (this.isCoordinateOutsideTheBorder(x, y))
            return value;

        return (this.drawMap[y][x] = value);
    }
}