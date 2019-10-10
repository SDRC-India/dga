import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import * as d3 from 'd3v3';
import { DataTreeService } from '../api/data-tree.service';
import { NgxSpinnerService } from 'ngx-spinner';
declare var $: any;

var hoverColor = ["#017A27", "#fb7c21", "#b7191c"];

var selectedAreaBubble;

@Component({
  selector: 'sdrc-bubble-chart',
  templateUrl: './bubble-chart.component.html',
  styleUrls: ['./bubble-chart.component.scss']
})
export class BubbleChartComponent implements OnInit, OnChanges {

  forceColorClicked:boolean=false;
  valueBubbleCalled:boolean=false;

  @Input()
  bubbleChartData;

  @Input()
  sectorId;

  @Input()
  timeperiodId;

  @Output()
  forceLayoutCalled = new EventEmitter();


  @Output()
  forceLayoutHover = new EventEmitter();

  chartData;

  tog1;
  parentTog;

  link;

  root;

  node;

  nodes;

  links;


  selectedAreaBubble;


  w = {
    "h": $(window).height(),
    "w": ($(window).width() * 90 / 100)
  };

  width;
  height;
  force;

  forceColor;

  bubbleNode;

  colorbubbleNode;

  forceLayout;

  svg;


  foci = {
    "#D13F43": {
      x: 200,
      y: 200
    },
    "#F19537": {
      x: 300,
      y: 200
    },
    "#22B369": {
      x: 400,
      y: 200
    }
  };
  constructor(private dataTreeService: DataTreeService,private ngxSpinnerService:NgxSpinnerService) {
  }

  ngOnInit() {

    this.initChart(this.bubbleChartData)
    $( "#search" ).on('input',(e)=>{
      this.searchNode();
     });

    $("#btnColorBubble")
      .click(
        () => {
          this.forceColorClicked=true;
          this.btnColorBubble(this.bubbleChartData)
          this.searchNode(); });
    $("#btnSortBubble")
      .click(
        () => { 
          this.ngxSpinnerService.show();
          setTimeout(d=>
            { this.btnSortBubble(this.bubbleChartData);
              this.searchNode();
              this.ngxSpinnerService.hide();
            },50)
    
         });

    $("#btnBack")
      .click(
        () => { 
          this.valueBubbleCalled=false
          this.forceColorClicked=false;
          this.initChart(this.bubbleChartData) 
          this.searchNode();
        });


  }

  btnSortBubble(data) {

    let root = {
      "name": "category",
      "children": data
    };
    
    let nodes = data;


    d3.select("#bubbleChart").selectAll("*").remove();

    let rScale = d3.scale.linear()
      .domain(
        [0, nodes.length])
      .range(
        [
          0,
          d3
            .max(
              nodes,
              (d: any) => {
                return d.size / 1.5;
              })]);
    let bubble = d3.layout
      .pack()
      .sort(
        this.comparator)
      .size(
        [
          this.width,
          this.height])
      .padding(15);
      this.svg= d3.select("#bubbleChart").append("svg").attr("width",
      this.width).attr("height", this.height);
    let layer1 = this.svg.append('g');
    let layer2 = this.svg.append('g');

    let node = layer2
      .selectAll(
        ".bubbleNode")
      .data(
        bubble
          .nodes(
            this.classes(root))
          .filter(
            function (
              d) {
              return !d.children;
            }))
      .enter()
      .append(
        "circle")
      .attr("class",
        "bubbleNode")
      .attr(
        "cx",
        function (
          d) {
          return d.x;
        })
      .attr(
        "cy",
        function (
          d) {
          return d.y;
        })
      
      .style(
        "opacity",
        0)
        
      .attr(
        "r",
        function (
          d) {

          if (nodes.length > 100) {
            return rScale(d.value) + 1;
          } else if (nodes.length > 40) {
            return rScale(d.value) / 2 + 1;
          } else if (nodes.length > 10
            && nodes.length < 40) {
            return rScale(d.value) / 4 + 1;
          } else if (nodes.length == 1) {
            return rScale(d.size) / 2.2 + 1;
          } else {
            return rScale(d.size) / 1.5 + 1;
          }


        })
      .style(
        "fill",
        function (
          d,
          i) {
          return d.color;
        })
        //bubble outline
      // .style(
      //   "stroke",
      //   "#099"
      //   )
        
      .on("dblclick",
        this.bubbleClickHandler.bind(this))
      .on(
        "mousemove",
        onmousemove)
      .on("mouseout",
        onmouseout)
      .on(
        "mouseover",
        onover)
        // .transition()
        // .duration(10000)
        // .ease("linear")
        
        ;
     
      let lineData = [];
      let sortedNode = [];
    function sortNode(a, b) {
      return (a.__data__.value - b.__data__.value);
    }
    sortedNode = node[0]
      .sort(sortNode);
    sortedNode
      .forEach(function (
        d) {
        lineData
          .push({
            'x': d.cx.animVal.value,
            'y': d.cy.animVal.value
          });
      });
      let lineFunction = d3.svg
      .line()
      .x(function (d) {
        return d.x;
      })
      .y(function (d) {
        return d.y;
      })
      .interpolate(
        "cardinal");
        let pathDraw = layer1
      .append("path")
      .attr(
        "d",
        lineFunction(lineData))
      .style(
        "opacity",
        0)
      .attr("stroke",
        "#099")
        .style(
          "stroke-width",
          "inherit"
        )
      .attr(
        "stroke-width",
        0)
      .attr("fill",
        "none");
    pathDraw
      .each(function () {
        d3
          .select(
            this)
          .transition()
          .duration(
            1000)
          .ease(
            "linear")
            //path of bubble
          .style(
            "opacity",
            0.35)
            .style(
              "stroke-width",
              "inherit"
            )
          .attr(
            "stroke-width",
            2);
      });

  }



  classes(root) {
    var classes = [];
    function recurse(
      name, node) {

      if (node.children)
        node.children
          .forEach(function (
            child) {
            recurse(
              node.name,
              child);
          });
      else
        classes
          .push({
            name: node.name,
            value: node.value,
            weight: node.weight,
            size: node.size,
            color: node.color,
            areaId: node.areaId
          });
    }
    recurse(null, root);
    return {
      children: classes
    };
  }

  comparator(a, b) {

    return (a.value - b.value);
  }


  btnColorBubble(data) {
    this.forceColor = null;
    this.colorbubbleNode = null;
    let nodes = data;


    d3.select("#bubbleChart").selectAll("*").remove();



    let rScale = d3.scale.linear()
      .domain(
        [0, nodes.length])
      .range(
        [
          0,
          d3
            .max(
              nodes,
              (d: any) => {
                return (d.size as number);
              })]);



    this.forceColor = d3.layout
      .force()
      .nodes(nodes)
      .gravity(0)
      // .charge(-70)
      .charge(
        (d) => {
          if (nodes.length > 60) {
            return -1
              * (Math
                .pow(
                  d.size * 3,
                  2.0) / nodes.length);
          } else
            return -1
              * (Math
                .pow(
                  d.size * 2.0,
                  2.0) / nodes.length);
        })
      .size(
        [this.width,
        this.height])
      .on("tick", this.tickk.bind(this))
      .start();






    this.svg = d3.select("#bubbleChart").append("svg").attr("width",
      this.width).attr("height", this.height);

    this.colorbubbleNode = this.svg.append("g").selectAll(".bubbleNode").data(
      nodes).enter().append("circle").attr(
        "class", "bubbleNode").attr("cx",
          (d) => {
            return d.x - 50;
          }).attr("cy", (d) => {
            return d.y;
          }).attr(
            "r",
            (d) => {
              if (nodes.length > 100) {
                return rScale(d.value) + 1;
              } else if (nodes.length > 40) {
                return rScale(d.value) / 2.5 + 1;
              } else if (nodes.length > 10 && nodes.length < 40) {
                return rScale(d.value) / 4 + 1;
              } else if (nodes.length == 1) {
                return rScale(d.size) / 2.2 + 1;
              } else {
                return rScale(d.size) / 1.5 + 1;
              }
            })
      .style("fill", (d, i) => {
        return d.color;
      }).style("cursor", "pointer").call(this.forceColor.drag).on(
        "dblclick", this.bubbleClickHandler.bind(this)).on(
          "mousemove", this.onmousemove.bind(this)).on("mouseout",
            onmouseout).on("mouseover", onover).on(
              "mousedown", function()  {
                d3.event.stopPropagation();
              });

              d3.select("#bubbleChart").on(
                "mousedown",
                this.mousedown.bind(this));
    this.svg
      .style("opacity",
        1e-6)
      .transition()
      .duration(1000)
      .style("opacity", 1);



  }


  tickk(d) {
    let k = .1 * d.alpha;
    this.bubbleChartData
      .forEach((
        o, i) => {

        o.y = o.y + (this.foci[o.color].y - o.y) * k

        o.x = o.x + (this.foci[o.color].x - o.x) * k

      });

    this.colorbubbleNode
      .attr(
        "cx",
        (
          d) => {
          return d.x - 50;
        })
      .attr(
        "cy",
        (
          d) => {
          return d.y;
        });
  }

  ngOnChanges(changes) {
    if (changes.bubbleChartData && !changes.bubbleChartData.firstChange) {
      this.valueBubbleCalled=false
      this.forceColorClicked=false;
      this.initChart(this.bubbleChartData)
    }
  }
  initChart(chartData) {
    if (!chartData) return;

    let nodes = chartData;
    this.force = undefined;
    this.bubbleNode = undefined;
    d3.select("#bubbleChart").selectAll("*").remove();

    let rScale = d3.scale.linear().domain(
      [0, nodes.length]).range(
        [0, d3.max(nodes, (d: any) => {
          return d.size as number;
        })]);

    this.width = $(window).width > 768 ? $("#home1")
      .width() : 600,
      this.height = this.w.h * 77.8 / 100;

      $('.dataTreeLegend').css("top",(this.height+15)+'px')
    this.force = d3.layout.force()
      .nodes(nodes)
      // .charge(-200)
      .charge(
        (d) => {
          if (nodes.length > 200) {
            return -1
              * (Math
                .pow(
                  d.size / 1.5,
                  3.8) / nodes.length);
          }
          if (nodes.length > 60) {
            return -1
              * (Math
                .pow(
                  d.size * 3.0,
                  2.0) / nodes.length);
          } else
            return -1
              * (Math
                .pow(
                  d.size * 3.0,
                  2.0) / nodes.length);
        }).size([this.width, this.height]).on(
          "tick", this.tick.bind(this)).start();



    this.svg = d3.select("#bubbleChart").append("svg").attr("width",
      this.width).attr("height", this.height);
    this.bubbleNode = this.svg.append("g").selectAll(".bubbleNode").data(
      nodes).enter().append("circle").attr(
        "class", "bubbleNode").attr("cx",
          (d) => {
            return d.x;
          }).attr("cy", (d) => {
            return d.y;
          }).attr(
            "r",
            (d) => {
              if (nodes.length >= 200) {
                return rScale(d.value * 1.5) + 1;
              }
              else if (nodes.length >= 40) {
                return rScale(d.value) / 2.0 + 1;
              } else
                if (nodes.length >= 10 && nodes.length < 40) {
                  return rScale(d.size) * 2 + 1;
                } else if (nodes.length == 1) {
                  return rScale(d.size) / 2.2 + 1;
                }
                else {
                  return rScale(d.size) / 1.5 + 1;
                }
            }).style("fill", (d, i) => {
              return d.color;
            }).style("cursor", "pointer").call(this.force.drag).on(
              "dblclick", this.bubbleClickHandler.bind(this)).on(
                "mousemove", this.onmousemove.bind(this)).on("mouseout",
                  onmouseout).on("mouseover", onover).on(
                    "mousedown", () => {
                      d3.event.stopPropagation();
                    });

    this.svg.style("opacity", 1e-6).transition().duration(
      1000).style("opacity", 1);

    d3.select("#bubbleChart").on("mousedown", this.mousedown.bind(this));
    this.force.resume();
  }

  onmousemove(d) {
  }


  mousedown() {
    this.bubbleChartData.forEach((o, i) => {
      o.x += (Math.random() - .5) * 150;
      o.y += (Math.random() - .5) * 150;
    });
    
    if(this.forceColorClicked)
    this.forceColor.resume();
    else
    this.force.resume();
    
  }

  tick() {
    this.bubbleNode.attr("cx", (d) => {
      return d.x;
    }).attr("cy", (d) => {
      return d.y;
    });

  }



  bubbleClickHandler(selectedArea) {

    onmouseoutAll();
    this.selectedAreaBubble=selectedArea;
    selectedAreaBubble=selectedArea;
    this.dataTreeService.getforceLayoutData(this.sectorId, selectedArea.areaId, this.timeperiodId).then(data => {
      this.valueBubbleCalled=true;
      d3.select("#bubbleChart").selectAll("*").remove();

     this.forceLayoutCalled.emit();

      this.forceLayout = d3.layout
        .force()
        .linkDistance(5)
        .charge(
          (
            d) => {

            return -400;
          })
        .size(
          [
            this.width,
            this.height])
        .linkDistance(
          (
            d) => {

            return d.target._children ? 100
              : 90;

          }).on(
            "tick",
            this.forceTick.bind(this));

      this.svg = d3.select("#bubbleChart").append("svg").attr("width",
        this.width).attr("height", this.height);
      this.tog1 = null;


                                    
      this.link = this.svg
        .selectAll(".link")
      this.node = this.svg
        .selectAll(".forceNode");
      this.root = data as any;

      this.root.children.forEach(this.collapse.bind(this));

      this.updateForce();



    }).catch(error => {

    })
  }




   dragend(d, i) {

    if (d.x > 4
        && d.x < this.width
        && d.y > 4
        && d.y < this.height) {
      d.fixed = true; // of
      

    } else {
      d.fixed = false;
    }
    this.forceLayout.resume();
  }

  updateForce() {

    let drag = this.forceLayout
    .drag()
    .on(
        "dragstart",
        dragstart)
    .on("dragend",
        this.dragend.bind(this));
        
    let nodes = this.flatten(this.root)


    this.links = d3.layout
      .tree()
      .links(
        nodes);



    this.forceLayout.nodes(
      nodes)
      .links(
        this.links)
      .start();


    this.link = this.link
      .data(
        this.links, (
          d) => {
          return d.target.id;
        });

    this.link.exit()
      .remove();



    this.link
      .enter()
      .insert(
        "line",
        ".forceNode")
      .attr(
        "class",
        "link");

    this.node = this.node
      .data(
        nodes,
        (d) => {
          return d.id;
        });

    this.node.exit()
      .remove();

    let nodeEnter = this.node
      .enter()
      .append("g")
      .attr(
        "class",
        "forceNode")
      .on(
        "click",
        (d,i,j)=>this.click(d,i,j))
      .on(
        "dblclick",
       dblclick)
      .style(
        "cursor",
        "pointer")
    .on(
      "mouseover",
        onForceover)
    .on(
      "mouseout",
      onmouseoutForce)
    .call(drag);

    nodeEnter
      .append(
        "circle")
      .attr(
        "r",
        function (
          d) {
          return Math
            .sqrt(d.size);
        })
      .style(
        "fill",
        function (
          d) {

          return d.color;
        });

    nodeEnter
      .append(
        "text")
      .style(
        "text-anchor",
        "start")
      .style(
        "font-size",
        function (
          d) {
          return "10px";
        })
      .style(
        "font-weight",
        "bold")
      .text(
        function (
          d) {
          let removedDesc = d.name
            .split(" ");
          if (d['_children'] != undefined && d['_children'].length) {
            return "*" + removedDesc
              .splice(
                0,
                1)
              .join(
                ".");
          }
          else {
            return removedDesc
              .splice(
                0,
                1)
              .join(
                ".");
          }

        });


    this.link = this.svg
      .selectAll(".link"),
      this.node = this.svg
        .selectAll("g.forceNode");
  }


  click(d, i, j) {

    if (d3.event.defaultPrevented)
      return;

    if (d.size == 300
      || d.size == 110) {

      this.updateForce();
    } else {

      if (d.children) {

        $(
          "#gridBtn")
          .hide();
        if (!d.lastLevel && this.tog1 && this.tog1.lastLevel && this.tog1.children) {
          this.tog1._children = this.tog1.children;
          this.tog1.children = null;
          this.updateForce();

        }
        d._children = d.children;
        d.children = null;
        this.tog1 = null;
        this.updateForce();

      } else if (d._children
        && !this.tog1) {

        d.children = d._children;
        d._children = null;
        this.tog1 = d;
        this.updateForce();
      }

      else if (d._children && this.tog1 && d.lastLevel) {
        d.children = d._children;
        d._children = null;
        if (this.tog1.lastLevel && this.tog1.children) {

          $(
            "#gridBtn")
            .hide();
          this.tog1._children = this.tog1.children;
          this.tog1.children = null;
          this.updateForce();

        }
        else if (!this.tog1.lastLevel) {
          this.parentTog = this.tog1;
        }
        this.tog1 = d;
        this.updateForce();
      }
      else if (d._children
        && this.tog1) {
        if (this.tog1.size != 300) {


          this.tog1._children = this.tog1.children;
          this.tog1.children = null;
          this.updateForce();
          if (this.tog1.lastLevel && this.tog1._children) {
            this.parentTog._children = this.parentTog.children;
            this.parentTog.children = null;
            this.updateForce();
          }
          d.children = d._children;
          d._children = null;
          this.tog1 = d;
          this.updateForce();
        }

      }
    }

    d3
      .select(
        j[i])
      .classed(
        "fixed",
        d.fixed = false);
    this.updateForce();

  }

  forceTick() {
    this.link
      .style(
        "stroke-width",
        (
          d) => {
          return "3px";
        })
      .attr(
        "x1",
        (
          d) => {
          return d.source.x;
        })
      .attr(
        "y1",
        (
          d) => {
          return d.source.y;
        })
      .attr(
        "x2",
        (
          d) => {
          if (d.target.x > this.width)
            return this.width - 20;
          else if (d.target.x < 5) {
            return 5;
          } else
            return d.target.x;
        })
      .attr(
        "y2",
        (
          d) => {
          if (d.target.y > this.height)
            return this.height - 20;
          else if (d.target.y < 5) {
            return 5;
          } else
            return d.target.y;
        });

    this.node
      .attr(
        "transform",
        (
          d) => {
          if (d.y > this.height)
            return "translate(" + d.x + "," + (this.height - 30) + ")";
          else if (d.y < 5)
            return "translate(" + d.x + "," + 10 + ")";
          else if (d.x > this.width)
            return "translate(" + (this.width - 30) + "," + d.y + ")";
          else if (d.x < 5)
            return "translate(" + 10 + "," + d.y + ")";
          else {
            return "translate(" + d.x + "," + d.y + ")";
          }
        });
  }


  flatten(root) {
    let nodes = [], i = 0;

    function recurse(
      node) {
      if (node.children)
        node.children
          .forEach(recurse);
      node.id = ++i;
      nodes
        .push(node);
    }
    recurse(root);
    return nodes;
  }


  collapse(d) {

    if (d.children) {
      d._children = d.children;
      d._children.forEach(this.collapse.bind(this));
      d.children = null;


    }
  }

   searchNode() {

    let selectedVal = (document.getElementById('search')as any).value;
  
    if (!this.valueBubbleCalled) {
      let node = d3.selectAll(".bubbleNode");
      node.style("opacity", "1");
      let optArray = [];
      if (selectedVal == "") {
        node.style("stroke-width", "1");
      }
  
      else {
        node.style("opacity", "0.2");
        let selected = node.filter(function(d, i) {
          
          if (d.name.toUpperCase().match(selectedVal.toUpperCase())) {
            optArray.push(d.name);
          }
          return d.name.toUpperCase().match(selectedVal.toUpperCase());
        });
        selected.style("opacity", "1");
  
      }
    } else {
      let node = d3.selectAll(".forceNode");
      node.style("opacity", "1");
     let  optArray = [];
      if (selectedVal == "") {
        node.style("stroke-width", "1");
      }
  
      else {
        node.style("opacity", "0.2");
        let selected = node.filter(function(d, i) {
  
          if (d.name.toUpperCase().match(selectedVal.toUpperCase())) {
            optArray.push(d.name);
          }
          return d.name.toUpperCase().match(selectedVal.toUpperCase());
        });
        selected.style("opacity", "1");
  
      }
    }
  
  }

}



function onover(d) {
  $(this)
    .popover({
      title: '',
      placement: 'top',
      container: 'body',
      trigger: 'manual',
      html: true,
      content: function () {
        return "<span style='color:#009999'>" + "<strong>" + "Facility Name : " + "</strong>" + "</span>" + d.name + "<br/>" + "<span style='color:#009999'>" + "<strong>" + "Score : " + "</strong>" + "</span>" + d.value + "%";
      }
    });
  $(this).popover('show');

  onhoverColor.call(this, d);
}


function onForceover(d) {
  let data;
  if (d.wt >= 0 && !d.response) {
    let value = d.value == "NaN" ? ""
        : d.value
            .toFixed(2);
    if(d.percentile){
  
      data= "<span style='color:#009999'>"
                + "Area Name : "
                + "</span>"
                + "<span>"
                + selectedAreaBubble.name
                + "</span>"
                + "<br/>"
                + "<span style='color:#009999'>"
                + "Question Name : "
                + "</span>"
                + "<span>"
                + d.qsname
                + "</span>"
                + "<br/>"
                + "<span style='color:#009999'>"
                + "Value : "
                + "</span>"
                + "<span>"
                + value
                + "</span>"
                + "<br/>"
                + "<span style='color:#009999'>"
                + "Max : "
                + "</span>"
                + "<span>"
                + d.wt
                + "</span>"
                + "<br/><span style='color:#009999'>Percentage : </span><span>"
                + d.percentile + "%"
                + "</span>";
    }
    else{
      data=
          "<span style='color:#009999'>"
              + "Area Name : "
              + "</span>"
              + "<span>"
              + selectedAreaBubble.name
              + "</span>"
              + "<br/>"
              + "<span style='color:#009999'>"
              + "Question Name : "
              + "</span>"
              + "<span>"
              + d.qsname
              + "</span>"
              + "<br/>"
              + "<span style='color:#009999'>"
              + "Value : "
              + "</span>"
              + "<span>"
              + value
              + "</span>"
              + "<br/>"
              + "<span style='color:#009999'>"
              + "Max : "
              + "</span>"
              + "<span>"
              + d.wt
              + "</span>"
              
    }


  }
  
  
  else if(d.response)
    {

      data=
            "<span style='color:#009999'>"
                + "Area Name : "
                + "</span>"
                + selectedAreaBubble.name

                + "<br/>"
                + "<span style='color:#009999'>"
                + "Question : "
                + "</span>"
                + d.qsname

                + "<br/>"
                + "<span style='color:#009999'>"
                + "Response : "
                + "</span>"
                + "<span>"
                + d.value
                + "</span>";
       


  }

  else {

    data=
            "<span style='color:#009999'>"
                + "Area Name : "
                + "</span>"
                + selectedAreaBubble.name

                + "<br/>"
                + "<span style='color:#009999'>"
                + "Question : "
                + "</span>"
                + d.qsname

                + "<br/>"
                + "<span style='color:#009999'>"
                + "Percentage : "
                + "</span>"
                + "<span>"
                + d.value
                + "%</span>";
        

  }

  $(this)
    .popover({
      title: '',
      placement: 'top',
      container: 'body',
      trigger: 'manual',
      html: true,
      content: data
    });
  $(this).popover('show');

}


function onmouseoutForce(d) {
  $('.popover').each(function () {
    $(this).remove();
  });
}

function onhoverColor(d) {
  d3.select(this).style('fill', (d: any, i) => {

    if (80 <= d.value) {
      return hoverColor[0];
    } else if (61 <= d.value && d.value <= 79) {
      return hoverColor[1];
    } else if (d.value <= 60) {
      return hoverColor[2];
    }
  });
}



function onmouseout(d) {
  $('.popover').each(function () {
    $(this).remove();
  });
  colormouseout.call(this, d);
}


function onmouseoutAll() {
  $('.popover').each(function () {
    $(this).remove();
  });

}

function colormouseout(d) {
  d3.select(this).style('fill', (d: any, i) => {
    return d.data ? d.data.color : d.color;;
  });
}

function dragstart(d) {
  d3
      .select(
          this)
      .classed(
          "fixed",
          d.fixed = true);
}


function dblclick(d) {
  d3
      .select(
          this)
      .classed(
          "fixed",
          d.fixed = false);
}


