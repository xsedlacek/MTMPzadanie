const express = require('express');
const app = express();
const port = 3000;

app.use(express.json());  // Pre spracovanie JSON requestov

function computeData(angle, speed) {
    const g = 9.81;
    const timeStop = (2 * speed * Math.sin(angle * Math.PI / 180)) / g;
    const timeInc = 0.1;
    let t = 0.0;
    const xCoords = [];
    const yCoords = [];
    const tCoords = [];

    while (t < timeStop) {
        const x = speed * t * Math.cos(angle * Math.PI / 180);
        const y = speed * t * Math.sin(angle * Math.PI / 180) - (g * t * t) / 2.0;
        xCoords.push(x);
        yCoords.push(y);
        tCoords.push(t);
        t += timeInc;
    }

    xCoords.push(speed * timeStop * Math.cos(angle * Math.PI / 180));
    yCoords.push(0.0);
    tCoords.push(timeStop);

    return xCoords.map((x, index) => ({
        x,
        y: yCoords[index],
        t: tCoords[index]
    }));
}


app.post('/compute', (req, res) => {
    const { angle, power } = req.body;
    console.log("Prijate data: ",req.body)
    if (typeof angle === 'number' && typeof power === 'number') {
        const data = computeData(angle, power);
        res.json(data);
    } else {
        res.status(400).json({ error: 'Invalid input' });
    }
});

app.listen(port, () => {
    console.log(`Server beží na http://localhost:${port}`);
});
