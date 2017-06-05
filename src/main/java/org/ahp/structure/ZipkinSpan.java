package org.ahp.structure;

public class ZipkinSpan {

	private Long trace_id;
	private Long span_id;
	private Long parent_id;
	private Long start_ts;
	private Long duration;
	private Long endpoint_ipv4;
	private String endpoint_service_name;
	private String name;

	public void setTrace_id(Long trace_id) {
		this.trace_id = trace_id;
	}

	public void setSpan_id(Long span_id) {
		this.span_id = span_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public void setStart_ts(Long start_ts) {
		this.start_ts = start_ts;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setEndpoint_ipv4(Long endpoint_ipv4) {
		this.endpoint_ipv4 = endpoint_ipv4;
	}

	public void setEndpoint_service_name(String endpoint_service_name) {
		this.endpoint_service_name = endpoint_service_name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTrace_id() {
		return trace_id;
	}

	public Long getSpan_id() {
		return span_id;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public Long getStart_ts() {
		return start_ts;
	}

	public Long getDuration() {
		return duration;
	}

	public Long getEndpoint_ipv4() {
		return endpoint_ipv4;
	}

	public String getEndpoint_service_name() {
		return endpoint_service_name;
	}

	public String getName() {
		return name;
	}
}
