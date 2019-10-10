import { Component, OnInit, Input, ElementRef, OnChanges } from '@angular/core';

import * as d3 from "d3";
declare var $: any;

@Component({
  selector: 'sdrc-radar-chart',
  templateUrl: './radar-chart.component.html',
  styleUrls: ['./radar-chart.component.scss']
})
export class RadarChartComponent implements OnInit, OnChanges {


  spiderdata={
    dataCollection:[]
  };

  @Input()
  radardata;

  windowWidth;
  width;
  dataValues = [];
  z;
  cfg = {
    radius: 5,
    w: this.width,
    h: this.width,
    factor: 1,
    factorLegend: .85,
    levels: 10,
    maxValue: 100,
    radians: 2 * Math.PI,
    opacityArea: 0.5,
    ToRight: 5,
    TranslateX: 80,
    TranslateY: 30,
    ExtraWidthX: 200,
    ExtraWidthY: 200,
    color: d3.scaleOrdinal(d3.schemeCategory10)
  };
  axis;
  allAxisSet: Set<String> = new Set();
  allAxis = []
  total: number = 0;
  Format = d3.format('%');
  radius = this.cfg.factor
    * Math.min(this.cfg.w / 2, this.cfg.h / 2);

  mouseOutcolor = ["#8FBBD9", "#FFBF87"];
  hoverColor = ["#1F77B4", "#FF8C26"];


  g;

  levelFactor;
  newX;
  newY;
  constructor(private hostRef: ElementRef) { }

  ngOnInit() {
    this.initChart()
  }

  ngOnChanges(changes) {
    if (changes.radardata && !changes.radardata.firstChange) {
      this.initChart()
    }
  }

  initChart() {
    d3.select("#radarChart").select("svg").remove();
    this.windowWidth = window.window;
    this.width = $(window).width() > 768 ? $(this.hostRef.nativeElement).parent().width() - 220 : $(this.hostRef.nativeElement).parent().width() - 120;
    this.cfg.w = this.width;
    this.cfg.h = this.width

    if ($(window).width() < 565) {
      this.cfg.TranslateX = 38;
    }
    this.allAxisSet = new Set();
    this.radardata.dataCollection.forEach(element => {
      element.forEach(data => {
        this.allAxisSet.add(data.axis);
      });
    });

    this.spiderdata.dataCollection=[]
let i=0;
    this.radardata.dataCollection.forEach(element => {
      let axis=[];
      element.forEach(data => {
        axis.push(data.axis);
      });
      
      this.spiderdata.dataCollection.push(element);

      this.allAxisSet.forEach(d=>{
        if(axis.indexOf(d)<0)
        {
          this.spiderdata.dataCollection[i].push({axis: d, value: "0", timePeriod: this.spiderdata.dataCollection[i][0].timePeriod,dataAvailable:false});
        }

        })
        i++;
    });

    this.allAxis = [];
    this.allAxis = Array.from(this.allAxisSet);
    this.total = this.allAxisSet.size;
    this.radius = this.cfg.factor
      * Math.min(this.cfg.w / 2, this.cfg.h / 2);
    this.drawSvg();

  }

  drawSvg() {
    this.g = d3.select("#radarChart").append("svg").style("overflow", "visible").attr(
      "width", this.cfg.w + this.cfg.ExtraWidthX).attr(
        "height", this.cfg.h + this.cfg.ExtraWidthY)
      .append("g").attr(
        "transform",
        "translate(" + this.cfg.TranslateX
        + "," + this.cfg.TranslateY
        + ")");
    this.drawCircularSegment();

  }

  drawCircularSegment() {
    // Circular segments
    for (let j = 0; j <= this.cfg.levels - 1; j++) {
      this.levelFactor = this.cfg.factor * this.radius
        * ((j + 1) / this.cfg.levels);
      this.g
        .selectAll(".levels")
        .data(this.allAxis)
        .enter()
        .append("svg:line")
        .attr(
          "x1",
          (d, i) => {
            return this.levelFactor
              * (1 - this.cfg.factor
                * Math
                  .sin(i
                    * this.cfg.radians
                    / this.total));
          })
        .attr(
          "y1",
          (d, i) => {
            return this.levelFactor
              * (1 - this.cfg.factor
                * Math
                  .cos(i
                    * this.cfg.radians
                    / this.total));
          })
        .attr(
          "x2",
          (d, i) => {
            return this.levelFactor
              * (1 - this.cfg.factor
                * Math
                  .sin((i + 1)
                    * this.cfg.radians
                    / this.total));
          })
        .attr(
          "y2",
          (d, i) => {
            return this.levelFactor
              * (1 - this.cfg.factor
                * Math
                  .cos((i + 1)
                    * this.cfg.radians
                    / this.total));
          })
        .attr("class", "line")
        .style("stroke", "grey")
        .style("stroke-opacity", "0.75")
        .style("stroke-width", "0.3px")
        .style("stroke-dasharray", 0)
        .attr(
          "transform",
          "translate("
          + (this.cfg.w / 2 - this.levelFactor)
          + ", "
          + (this.cfg.h / 2 - this.levelFactor)
          + ")");
    }

    // Text indicating at what % each level is
    for (let j = 0; j < this.cfg.levels; j++) {
      this.levelFactor = this.cfg.factor * this.radius
        * ((j + 1) / this.cfg.levels);
      this.g
        .selectAll(".levels")
        .data([1])
        .enter()
        .append("svg:text")
        .attr(
          "x",
          (d) => {
            return this.levelFactor
              * (1 - this.cfg.factor
                * Math
                  .sin(0));
          })
        .attr(
          "y",
          (d) => {
            return this.levelFactor
              * (1 - this.cfg.factor
                * Math
                  .cos(0));
          })
        .attr("class", "legend")
        .style("font-family", "sans-serif")
        .style("font-size", "10px")
        .attr(
          "transform",
          "translate("
          + (this.cfg.w
            / 2
            - this.levelFactor + this.cfg.ToRight)
          + ", "
          + (this.cfg.h / 2 - this.levelFactor)
          + ")").attr("fill",
            "#737373").text(
              Math.round((j + 1) * 100
                / this.cfg.levels));
    }

    this.drawRadarChart();
  }

  drawRadarChart() {
    let series = 0;
    this.axis = this.g.selectAll(".axis").data(this.allAxis)
      .enter().append("g").attr("class",
        "axis");

    this.axis
      .append("line")
      .attr("x1", this.cfg.w / 2)
      .attr("y1", this.cfg.h / 2)
      .attr(
        "x2",
        (d, i) => {
          return this.cfg.w
            / 2
            * (1 - this.cfg.factor
              * Math
                .sin(i
                  * this.cfg.radians
                  / this.total));
        })
      .attr(
        "y2",
        (d, i) => {
          return this.cfg.h
            / 2
            * (1 - this.cfg.factor
              * Math
                .cos(i
                  * this.cfg.radians
                  / this.total));
        }).attr("class", "line").style(
          "stroke", "grey").style(
            "stroke-width", "1px");

    this.axis
      .append("text")
      .attr("class", "legend")
      .text((d) => {
        if (d.split(' ').length > 3)
          return d.split(' ').slice(0, 3).join(" ") + " ...";
        else
          return d;
      })
      .style("font-family", "sans-serif")
      .style("font-size", "10px")
      .attr("text-anchor", "start")
      .attr("dy", "1.5em")
      .attr("transform", (d, i) => {
        return "translate(0, -10)";
      })

      .attr(
        "x",
        (d, i) => {
          return this.cfg.w
            / 2
            * (1 - this.cfg.factorLegend
              * Math
                .sin(i
                  * this.cfg.radians
                  / this.total))
            - 60
            * Math
              .sin(i
                * this.cfg.radians
                / this.total);
        })
      .attr(
        "y",
        (d, i) => {

          return this.cfg.h
            / 2
            * (1 - Math
              .cos(i
                * this.cfg.radians
                / this.total))
            - 20
            * Math
              .cos(i
                * this.cfg.radians
                / this.total);
        });

    this.spiderdata.dataCollection
      .forEach((y, x) => {
        this.dataValues = [];
        this.g
          .selectAll(".nodes")
          .data(
            y,
            (j, i) => {
              this.dataValues
                .push([
                  this.cfg.w
                  / 2
                  * (1 - (parseFloat(Math.max(j.value, 0).toString()) / this.cfg.maxValue)
                    * this.cfg.factor
                    * Math
                      .sin(i
                        * this.cfg.radians
                        / this.total)),
                  this.cfg.h
                  / 2
                  * (1 - (parseFloat(Math.max(j.value, 0).toString()) / this.cfg.maxValue)
                    * this.cfg.factor
                    * Math
                      .cos(i
                        * this.cfg.radians
                        / this.total))]);
            });

        this.dataValues.push(this.dataValues[0]);
        this.g
          .selectAll(".area")
          .data([this.dataValues])
          .enter()
          .append("polygon")
          .attr(
            "class",
            "radar-chart-serie"
            + series)
          .style("stroke-width",
            "2px")
          .style("stroke",
            this.cfg.color(series as any))
          .attr(
            "points",
            (d) => {
              let str = "";
              for (let pti = 0; pti < d.length; pti++) {
                str = str
                  + d[pti][0]
                  + ","
                  + d[pti][1]
                  + " ";
              }
              return str;
            })
          .style("fill-opacity",
            this.cfg.opacityArea)
          .style(
            "fill",
            (j, i) => {
              return this.cfg.color(series as any);
            })

          .on(
            'mouseover',
            (d, i, n) => {
              this.z = "polygon."
                + d3
                  .select(
                    n[i])
                  .attr(
                    "class");
              this.g
                .selectAll(
                  "polygon")
                .transition(
                  200)
                .style(
                  "fill-opacity",
                  0.1);
              this.g
                .selectAll(
                  this.z)
                .transition(
                  200)
                .style(
                  "fill-opacity",
                  .7);
            })
          .on(
            'mouseout',
            () => {
              this.g
                .selectAll(
                  "polygon")
                .transition(
                  200)
                .style(
                  "fill-opacity",
                  this.cfg.opacityArea);
            });

        let rect = d3.select("#radarChart svg g").append("rect")

        rect.attr("x", this.width / 2 - 40)
        rect.attr("y", this.width + 50 + x * 30);
        rect.attr("width", "20px");
        rect.attr("height", "20px");
        rect.style("width", "20px");
        rect.style("height", "20px");
        rect.style("fill", this.cfg.color(series as any));

        let timeText = d3.select("#radarChart").select("svg g").append("text");
        timeText.attr("x", this.width / 2 - 10)
        timeText.attr("y", this.width + 65 + x * 30);

        timeText.text(y[0].timePeriod);
        series++;
      });
    series = 0;


    this.spiderdata.dataCollection.forEach((y, x) => {
      this.g.selectAll(".nodes")
        .data(y).enter()
        .append("svg:circle")
        .attr("class", "radar-chart-series" + series)
        .attr('r', this.cfg.radius)
        .attr("alt", (j) => { return Math.max(j.value, 0) })
        .attr("cx", (j, i) => {
          this.dataValues.push([
            this.cfg.w / 2 * (1 - (parseFloat(Math.max(j.value, 0).toString()) / this.cfg.maxValue) * this.cfg.factor * Math.sin(i * this.cfg.radians / this.total)),
            this.cfg.h / 2 * (1 - (parseFloat(Math.max(j.value, 0).toString()) / this.cfg.maxValue) * this.cfg.factor * Math.cos(i * this.cfg.radians / this.total))
          ]);
          return this.cfg.w / 2 * (1 - (Math.max(j.value, 0) / this.cfg.maxValue) * this.cfg.factor * Math.sin(i * this.cfg.radians / this.total));
        })
        .attr("cy", (j, i) => {
          return this.cfg.h / 2 * (1 - (Math.max(j.value, 0) / this.cfg.maxValue) * this.cfg.factor * Math.cos(i * this.cfg.radians / this.total));
        })
        // .attr("data-id", (j) => { return console.log((j.axis as string).replace("-" ," ").replace("'"," " )) })
        .style("fill", (
          d,
          i) => {
            if(d.dataAvailable)
          return this.cfg.color(series as any);
          else
          return '#524646';
   
        })
        .style("fill-opacity", .9)
        .on('mouseover', function showPopover(d) {
          $(this).popover({
            title: '',
            placement: 'top',
            container: 'body',
            trigger: 'manual',
            html: true,
            content: function () {
              return "<div style='color: #257ab6;'>" + d.axis + "</div>" +
              "<div> Time Period : " + d.timePeriod +
              "</div>" + "Score : " + (d.dataAvailable? (d.value + "%"):"Data Not Available");
            }
          });
          $(this).popover('show');
        })
        .on("mouseout", function showPopover(d) {
          $('.popover').each(function () {
            $(this).remove();
          });
        });

      series++;
    });
    if ($(window).width() > 991)
      $("table.assign-height-similar-spider").height($("#home").width() - 106);
  }


}
