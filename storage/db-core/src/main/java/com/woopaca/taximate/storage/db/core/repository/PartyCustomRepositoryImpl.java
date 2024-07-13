package com.woopaca.taximate.storage.db.core.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.woopaca.taximate.storage.db.core.entity.QPartyEntity.partyEntity;

@Repository
public class PartyCustomRepositoryImpl implements PartyCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final GeometryFactory geometryFactory;

    public PartyCustomRepositoryImpl(JPAQueryFactory queryFactory, GeometryFactory geometryFactory) {
        this.queryFactory = queryFactory;
        this.geometryFactory = geometryFactory;
    }

    @Override
    public List<PartyEntity> findContainsDepartureAfter(double minLatitude, double minLongitude,
                                                        double maxLatitude, double maxLongitude,
                                                        LocalDateTime after) {
        Polygon polygon = geometryFactory.createPolygon(new Coordinate[]{
                new Coordinate(minLongitude, minLatitude),
                new Coordinate(maxLongitude, minLatitude),
                new Coordinate(maxLongitude, maxLatitude),
                new Coordinate(minLongitude, maxLatitude),
                new Coordinate(minLongitude, minLatitude)
        });
        BooleanExpression contains = Expressions.booleanTemplate(
                "ST_Contains({0}, {1})", polygon, partyEntity.originLocation
        );
        return queryFactory.select(partyEntity)
                .from(partyEntity)
                .where(contains.and(partyEntity.departureTime.after(after)))
                .orderBy(partyEntity.departureTime.asc())
                .fetch();
    }
}
