(function(t){var a=t.seriesTypes,n=t.Chart.prototype,e=t.getOptions(),i=t.extend;i(e.lang,{noData:"No data to display"});e.noData={position:{x:0,y:0,align:"center",verticalAlign:"middle"},attr:{},style:{fontWeight:"bold",fontSize:"12px",color:"#60606a"}};function o(){return!!this.points.length}a.pie.prototype.hasData=o;if(a.gauge){a.gauge.prototype.hasData=o}if(a.waterfall){a.waterfall.prototype.hasData=o}t.Series.prototype.hasData=function(){return this.dataMax!==undefined&&this.dataMin!==undefined};n.showNoData=function(a){var t=this,n=t.options,e=a||n.lang.noData,o=n.noData;if(!t.noDataLabel){t.noDataLabel=t.renderer.label(e,0,0,null,null,null,null,null,"no-data").attr(o.attr).css(o.style).add();t.noDataLabel.align(i(t.noDataLabel.getBBox(),o.position),false,"plotBox")}};n.hideNoData=function(){var a=this;if(a.noDataLabel){a.noDataLabel=a.noDataLabel.destroy()}};n.hasData=function(){var a=this,t=a.series,n=t.length;while(n--){if(t[n].hasData()&&!t[n].options.isInternal){return true}}return false};function l(){var a=this;if(a.hasData()){a.hideNoData()}else{a.showNoData()}}n.callbacks.push(function(a){t.addEvent(a,"load",l);t.addEvent(a,"redraw",l)})})(Highcharts);