function PbChartCtrl($scope, $log, uiTranslateFilter) {

  var chart = null;
  function totalCasesChart(data, ctx) {
          chart = new Chart(ctx, getEstructuraGrafica(data))
      }
  
      function renderCharts() {
        const ctx = document.querySelector('#'+$scope.properties.idCanvas)
        const data = { "info": $scope.properties.data}
        totalCasesChart(data, ctx);
      }

    $scope.$watch('properties.data', function(value) {
      if(chart != null){
          chart.data.datasets[0].data = ($scope.properties.data.map(item => item.valorConvertido));
          chart.update();
      }else if( $scope.properties.data.length > 0){
          renderCharts();
      }
    });



    function getEstructuraGrafica(data){
      const {
          info
        } = data
      let strChart = {
          type: 'line',
          data: {
            labels: $scope.properties.labels,
            datasets: [
              {
                borderColor: 'rgb(255, 89, 0)',
                data: info.map(item => item.valorConvertido),
                tension: 0,
                fill: false
              }
            ]
          },
          options: {
              maintainAspectRatio: false,
              layout: {
                padding: 20
              },
              plugins: {
                  legend: {
                      display: false,
                  },
                  tooltip:{
                      displayColors:false,
                      callbacks:{
                          label: function(label) {
                              let info="";
                              $scope.properties.data.forEach( datos =>{
                                  if(label.raw == datos.valorConvertido && label.label == datos.letra){
                                      info = datos.valorOriginal;
                                  }
                              })
                              return label.label + ':' +  info;
                          }
                      }
                      
                  }
              },
            scales: {
              x: {
                
                ticks:{
                  padding:10
                }
              },
              x2:{
                  ticks:{
                      callback: function(index) {
                          return $scope.properties.data[index].valorOriginal;
                      }
                  }
              },
              y:{
                  min:30,
                  ticks:{
                    padding:20
                  }
              }
            },
            elements: {
              line: {
                borderWidth: 4,
                fill: false,
              },
              point: {
                radius: 6,
                borderWidth: 4,
                backgroundColor: 'white',
                hoverRadius: 8,
                hoverBorderWidth: 4,
              }
            }
          }
        }

        return strChart;
      
    }
  }
  