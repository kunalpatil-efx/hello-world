//POST https://postman-echo.com/post
//Body
/*
{
	"labels": [
		"{{$randomCountryCode}}",
		"{{$randomCountryCode}}",
		"{{$randomCountryCode}}",
		"{{$randomCountryCode}}",
		"{{$randomCountryCode}}"
	],
	"data": [
		{{$randomInt}},
		{{$randomInt}},
		{{$randomInt}},
		{{$randomInt}},
		{{$randomInt}}
	]
}
*/

//Test
// Access the response data JSON as a JavaScript object
const res = pm.response.json();

// -----------------------------
// - Structure data for charts -
// -----------------------------

// EDIT THIS OBJECT TO BIND YOUR DATA
const vizData = {
    
    // Labels take an array of strings
    labels: res.data.labels,
    
    // Data takes an array of numbers
    data: res.data.data
};

// ------------
// - Template -
// ------------

// Configure the template
var template = `
<canvas id="myChart" height="75"></canvas>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script> 

<script>
    // Get DOM element to render the chart in
    var ctx = document.getElementById("myChart");

    // Configure Chart JS here.
    // You can customize the options passed to this constructor to
    // make the chart look and behave the way you want
    var myChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: [], // We will update this later in pm.getData()
            datasets: [{
                data: [], // We will update this later in pm.getData()
                
                // Change these colours to customize the chart
                backgroundColor: ["#003f5c", "#58508d", "#bc5090", "#ff6361", "#ffa600"],
            }]
        },
        options: {
            legend: { display: false },
            title: {
                display: true,
                text: 'Count'
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Items'
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Count'
                    }
                }]
            }
        }

    });

    // Access the data passed to pm.visualizer.set() from the JavaScript
    // code of the Visualizer template
    pm.getData(function (err, value) {
        myChart.data.datasets[0].data = value.data;
        myChart.data.labels = value.labels;
        myChart.update();
    });

</script>`;

// -------------------------
// - Bind data to template -
// -------------------------

// Set the visualizer template
pm.visualizer.set(template, vizData);
