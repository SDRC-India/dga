import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import * as d3 from 'd3';
import * as $ from 'jquery'
declare var $: any;
import * as _ from 'lodash';

@Component({
  selector: 'sdrc-data-tree',
  templateUrl: './sdrc-data-tree.component.html',
  styleUrls: ['./sdrc-data-tree.component.scss']
})
export class SdrcDataTreeComponent implements OnInit, OnChanges {

  @Input()
  dataTreeData;

  @Output()
  selectedSector = new EventEmitter();

  w = {
    "h": $(window).height(),
    "w": ($(window).width() * 90 / 100)
  };
  sear;
  width;
  height;
  margin = {
    top: 20,
    right: 120,
    bottom: 20,
    left: 100
  };
  i = 0;
  duration = 750;
  root;
  tree;
  diagonal;
  svg
  source;
  tog = null;
  ui;
  textr = [];
  ndarr = [];
  ancestors = [];
  animGroup;


  constructor() { }

  ngOnInit() {
    this.initChart()
  }

  ngOnChanges(changes) {
    if (changes.dataTreeData && !changes.dataTreeData.firstChange && this.dataTreeData) {
      this.initChart()
    }
  }


  initChart() {
    if (!this.dataTreeData) return;
    this.i = 0;
    this.duration = 750;
    d3.select("#sdrcDataTree").selectAll("*").remove();
    this.width = 500;
    this.height = this.w.h * 80 / 100;
    this.tree = d3.tree().size([this.height, this.width]);

    this.svg = d3.select("#sdrcDataTree").append("svg").attr("width", this.width).attr(
      "height", this.height + this.margin.bottom).append("g").attr(
        "transform",
        "translate(" + this.margin.left + "," + this.margin.top + ")");
    this.root = this.dataTreeData;
    this.root.x0 = this.height / 2;
    this.root.y0 = 0;

    this.source;
    this.tog = null;
    this.ui = null;
    this.textr = [];
    this.ndarr = [];
    this.ancestors = [];
    this.svg.append("svg:clipPath").attr("id", "clipper").append("svg:rect")
      .attr('id', 'clip-rect');

    this.animGroup = this.svg.append("svg:g").attr("clip-path",
      "url(#clipper)");
    this.root.children.forEach(this.collapse.bind(this));
    d3.select(self.frameElement).style("height", "400px");

    this.updateTree(this.root);
  }

  collapse(d) {
    if (d.name != this.dataTreeData.children[2].name) {
      if (d.children) {
        d._children = d.children;
        d._children.forEach(this.collapse.bind(this));
        d.children = null;
      }
    } else {
      this.tog = d;
    }

  }

  updateTree(source) {
    let sources: any = d3.hierarchy(this.root);
    sources = this.tree(sources)
    let nodes = sources.descendants()
    let links = nodes.slice(1)



    nodes.forEach((d: any) => {
      d.y = d.depth * 180;
    });


    let node = this.svg.selectAll("g.node").data(nodes, (d) => {
      return d.id || (d.id = ++this.i);
    });
    let nodeEnter = node.enter().append("g").attr("class", "node")
      .attr(
        "transform",
        (d) => {
          return "translate(" + sources.y + ","
            + sources.x + ")";
        }).on("click", (d) => {
          if (d.parent) {
            return this.click(d);
          }
        })

      .on("mouseover", function showPopover(d) {
        if (d.depth == 2) {
          $(this).popover(

            {
              title: '',
              placement: 'left',
              container: 'body',
              trigger: 'manual',
              html: true,
              content: function () {
                return d.data.name;
              }
            });
          $(this).popover('show');
        }
      }).on("mouseout", (d) => {
        this.removePopovers(d);
      });

    nodeEnter.append("circle").attr('class', 'node').attr("r", 1e-6).style("fill",
      "#d0d0d0").style(
        "z-index", "-1")
      .style("cursor", "pointer")



    nodeEnter.append("text").style("fill", (d) => {
      return "black";
    }).style("font-weight", "bold").style("font-size", (d) => {
      if (d.depth == 1)
        return "8px";
      else
        return "10px";

    }).attr("x", (d) => {
      if (!d.parent) {
        return -80;
      } else {
        return d.children || d._children || d.depth == 1 ? -8 : 20;
      }

    }).attr("dy", (d) => {
      if (!d.parent)
        return ".35em"
      else
        return ".35em"
    })

      .text((d) => {
        if (d.data.name.includes("."))
          return d.data.name.split(".")[0];
        else
          return d.data.name
      }).style("fill-opacity", 1e-6);

    let nodeUpdate = nodeEnter.merge(node);
    nodeUpdate.transition().duration(this.duration).attr(
      "transform", (d) => {
        return "translate(" + d.y + "," + d.x + ")";
      });


    nodeUpdate.select("circle").attr("id", "nodeEmp").attr("r", 14)
      .style("stroke", "none");

    nodeUpdate.select("text").style("fill-opacity", 1);


    // Transition exiting nodes to the parent's new position.
    let nodeExit = node.exit().transition().duration(this.duration)
      .attr(
        "transform",
        (d) => {
          return "translate(" + sources.y + ","
            + sources.x + ")";
        }).remove();

    nodeExit.select("circle").attr("r", 1e-6);

    nodeExit.select("text").style("fill-opacity", 1e-6);

    // Update the links

    let link = this.svg.selectAll("path.link").attr("id", "pathform")
      .data(links)

    // Enter any new links at the parent's previous position.#####
    link.enter().insert("path", "g").attr("class", "link")
      .style("stroke", "#D0D0D0")
      .style("fill", "none")
      .style("stroke-width", 3)
      .style("opacity", 1)
      .attr("d", (d) => {
        return this.linkMethod(d);
      });


    // Transition links to their new position.
    link.transition().duration(this.duration).attr("d", this.linkMethod.bind(this));

    // Transition exiting nodes to the parent's new position.
    link.exit().transition().duration(this.duration).attr("d",
      (d) => {
        let o = {
          x: sources.x,
          y: sources.y
        };
        return this.linkMethod(d);
      }).remove();

    // clear the old positions for transition.
    nodes.forEach((d) => {
      d.x0 = d.x;
      d.y0 = d.y;
    });

    this.ui = {
      svgRoot: this.svg,
      nodeGroup: node,
      linkGroup: link,
      animGroup: this.animGroup
    };

    if (source.name == 'Facility Level') {
      this.click(source.children[0]);

    }
  }

  showPopover(d) {
    if (d.depth == 2) {
      $(this).popover(

        {
          title: '',
          placement: 'auto left',
          container: 'body',
          trigger: 'manual',
          html: true,
          content: () => {
            return d.name;
          }
        });
      $(this).popover('show');

    }
  }
  removePopovers(d) {
    $('.popover').each(function () {
      $(this).remove();
    });
  }
  click(d) {

    if (this.tog == d || this.tog == d.data)
      return;

    this.sear = true;
    $("#gridBtn").hide();
    this.ui.animGroup.selectAll("path.selected").data([]).exit()
      .remove();
    if (!d.children) {
      if (d.data) d = d.data
      if (d._children) {
        d.children = d._children;
        d._children = null;
        this.tog._children = this.tog.children;
        this.tog.children = null;
        this.updateTree(this.tog);
        this.tog = d;
        this.updateTree(d);
      }
    }


    let ancestors = [];

    let parent;
    let sources = d3.hierarchy(this.root);
    this.tree(sources)
    let nodes = sources.descendants()
    if (d.children) {
      if (d.children.length > 1) {
        parent = d.children[1];

      } else {
        parent = d.children[0];

      }
    } else {
      parent = d;
    }
    this.selectedSector.emit(parent);

    parent = nodes.filter(e => e.data.Id == parent.Id)[0]

    while (parent) {
      ancestors.push(parent);
      parent = parent.parent;

    }
    // Get the matched links

    let matchedLinks = [];

    d3.select("#sdrcDataTree").selectAll('path.link').filter((e: any) => {
      return _.some(ancestors, (p) => {
        return p.data === e.data;
      });
    }).each((k) => {
      matchedLinks.push(k);
    });

    this.animateNode(ancestors[0]);
    this.animateLink(matchedLinks);


  }

  linkMethod(d) {
    return "M" + d.y + "," + d.x
      + "C" + (d.parent.y + 100) + "," + d.x
      + " " + (d.parent.y + 100) + "," + d.parent.x
      + " " + d.parent.y + "," + d.parent.x;
  }

  animateLink(links) {



    // Links defined in highlightEvent
    this.ui.animGroup.selectAll("path.selected").data([]).exit()
      .remove();

    this.ui.animGroup.selectAll("path.selected").data(links).enter()
      .append("svg:path").attr("class", "selected").style(
        "fill", "none").style("stroke", "#009999")
      .style("stroke-width", 3).style("opacity", 1).attr("d",
        this.linkMethod.bind(this));

    // Animate the path selected
    let overlayBox = this.ui.svgRoot.node().getBBox();

    this.ui.svgRoot.select("#clip-rect")

      .attr("x", overlayBox.x).attr("width", 0).transition()
      .duration(1200).attr("y",
        overlayBox.y + overlayBox.width).attr("height",
          overlayBox.height).attr("width",
            overlayBox.width).attr("y", overlayBox.y);

  }

  animateNode(node) {
    let nodes = d3.select("#sdrcDataTree").selectAll("g.node");
    this.ndarr = [];
    while (node) {
      this.ndarr.push(node);
      node = node.parent;
    }


    nodes.selectAll("text").style("fill", (o: any) => {
      if (_.some(this.ndarr, (p) => {
        return p.data === o.data && o.depth == 1;
      })
      ) {
        return "white";

      } else {

        return "black";
      }
    });

    nodes.selectAll("circle.node").style("fill", (o: any) => {
      if (_.some(this.ndarr, (p) => {
        return p.data === o.data;
      })
      ) {
        return "#009999";

      } else {

        return "#D0D0D0";
      }
    });

  }

}
