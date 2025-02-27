package com.neueda.jetbrains.plugin.graphdb.database.nebula.data;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphNode;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphPropertyContainer;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphRelationship;
import com.vesoft.nebula.client.graph.data.Relationship;
import com.vesoft.nebula.client.graph.data.ValueWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务说明：
 *
 * @Author jiangyiwang-jk
 * @Date 2023/12/29 15:54
 */
public class NebulaGraphRelationship implements GraphRelationship {

    private Relationship relationship;

    public NebulaGraphRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public String getId() {
        return getStartNodeId() + "#" + getEndNodeId();
    }

    @Override
    public GraphPropertyContainer getPropertyContainer() {
        return () -> {
            try {
                HashMap<String, ValueWrapper> properties = relationship.properties();
                Map<String, Object> map = new HashMap<>();
                for (Map.Entry<String, ValueWrapper> entry : properties.entrySet()) {
                    map.put(entry.getKey(), entry.getValue().getValue().getFieldValue());
                }
                return map;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public List<String> getTypes() {
        return Arrays.asList(relationship.edgeName());
    }

    @Override
    public String getTypesName() {
        return relationship.edgeName();
    }

    @Override
    public boolean hasStartAndEndNode() {
        return true;
    }

    @Override
    public String getStartNodeId() {
        return String.valueOf(this.relationship.srcId().getValue().getFieldValue());
    }

    @Override
    public String getEndNodeId() {
        return String.valueOf(this.relationship.dstId().getValue().getFieldValue());
    }

    @Override
    public GraphNode getStartNode() {
        return new NebulaGraphNode(getStartNodeId());
    }

    @Override
    public GraphNode getEndNode() {
        return new NebulaGraphNode(getEndNodeId());
    }

    @Override
    public boolean isTypesSingle() {
        return false;
    }
}
